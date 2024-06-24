package com.nafi.airseat.presentation.flightdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import com.nafi.airseat.R
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.data.source.network.model.booking.BookingPassenger
import com.nafi.airseat.data.source.network.model.booking.OrderedBy
import com.nafi.airseat.databinding.ActivityFlightDetailBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.payment.WebViewMidtransActivity
import com.nafi.airseat.utils.ApiErrorException
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.showSnackBarError
import com.nafi.airseat.utils.toConvertDateFormat
import com.nafi.airseat.utils.toIndonesianFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class FlightDetailPriceActivity : AppCompatActivity() {
    private val binding: ActivityFlightDetailBinding by lazy {
        ActivityFlightDetailBinding.inflate(layoutInflater)
    }

    private val flightDetailPriceViewModel: FlightDetailPriceViewModel by viewModel()
    private val paymentMethod = "snap"
    private var flightDetail: FlightDetail? = null
    private lateinit var passengerList: MutableList<BookingPassenger>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val tax = intent.getDoubleExtra("tax", 0.0)
        val promo = intent.getDoubleExtra("promo", 0.0)
        val flightId = intent.getStringExtra("idDepart")
        val idReturn = intent.getIntExtra("idReturn", 0)
        val fullName = intent.getStringExtra("full_name")
        val phoneNumber = intent.getStringExtra("number_phone")
        val email = intent.getStringExtra("email")
        val familyName = intent.getStringExtra("family_name")
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()

        if (idReturn != 0) {
            binding.layoutFlightDetailPrice.llFlightDetailReturnTicket.isVisible = true
            proceedDetailTicket(flightId.toString())
            proceedDetailReturnTicket(idReturn.toString())
        } else {
            proceedDetailTicket(flightId.toString())
            binding.layoutFlightDetailPrice.llFlightDetailReturnTicket.isVisible = false
        }

        bindPrice()

        passengerList.forEach { passenger ->
            Log.d("FlightDetailPriceActivity", "Passenger: $passenger & $idReturn & $flightId")
        }

        Log.d("OrderedBy", "Ordered Bio: $fullName, $familyName, $phoneNumber, $email")

        binding.cvSectionCheckout.btnPayment.setOnClickListener {
            createBooking()
        }
        binding.ibBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bindPrice() {
        val price = intent.getIntExtra("price", 0).toDouble()
        val priceBaby = 0.0
        val adults = intent.getIntExtra("adults", 0)
        val baby = intent.getIntExtra("baby", 0)
        val child = intent.getIntExtra("child", 0)
        var totalPrice = 0.0

        if (adults > 0) {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceAdults.visibility = View.VISIBLE
            binding.layoutFlightDetailPrice.layoutPrice.tvAdults.text = "$adults Adult${if (adults > 1) "s" else ""}"
            val totalPriceAdult = price * adults
            binding.layoutFlightDetailPrice.layoutPrice.tvAdultPrice.text = totalPriceAdult.toIndonesianFormat()
            totalPrice += totalPriceAdult
        } else {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceAdults.visibility = View.GONE
        }

        if (child > 0) {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceChild.visibility = View.VISIBLE
            binding.layoutFlightDetailPrice.layoutPrice.tvChild.text = "$child Child${if (child > 1) "ren" else ""}"
            val totalPriceChild = price * child
            binding.layoutFlightDetailPrice.layoutPrice.tvChildPrice.text = totalPriceChild.toIndonesianFormat()
            totalPrice += totalPriceChild
        } else {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceChild.visibility = View.GONE
        }

        if (baby > 0) {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceBaby.visibility = View.VISIBLE
            binding.layoutFlightDetailPrice.layoutPrice.tvBaby.text = "$baby Baby${if (baby > 1) "ies" else ""}"
            binding.layoutFlightDetailPrice.layoutPrice.tvBabyPrice.text = priceBaby.toIndonesianFormat()
        } else {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceBaby.visibility = View.GONE
        }
        binding.cvSectionCheckout.totalPrice.text = totalPrice.toIndonesianFormat()
    }

    private fun proceedDetailTicket(flightId: String) {
        flightDetailPriceViewModel.getDetailFlight(flightId).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.csvDetailFlight.setState(ContentState.SUCCESS)
                    binding.layoutFlightDetailPrice.root.visibility = View.VISIBLE
                    it.payload?.let { detail ->
                        flightDetail = detail
                        bindView(detail)
                    }
                },
                doOnLoading = {
                    binding.csvDetailFlight.setState(ContentState.LOADING)
                    binding.layoutFlightDetailPrice.root.visibility = View.GONE
                },
                doOnError = {
                    if (it.exception is NoInternetException) {
                        binding.csvDetailFlight.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvDetailFlight.setState(
                            ContentState.ERROR_GENERAL,
                        )
                    }
                },
                doOnEmpty = {
                    binding.csvDetailFlight.setState(ContentState.EMPTY, desc = "Data not found")
                },
            )
        }
    }

    private fun proceedDetailReturnTicket(idReturn: String) {
        flightDetailPriceViewModel.getDetailFlight(idReturn).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.csvDetailFlight.setState(ContentState.SUCCESS)
                    binding.layoutFlightDetailPrice.root.visibility = View.VISIBLE
                    it.payload?.let { detail ->
                        flightDetail = detail
                        bindViewReturn(detail)
                    }
                },
                doOnLoading = {
                    binding.csvDetailFlight.setState(ContentState.LOADING)
                    binding.layoutFlightDetailPrice.root.visibility = View.GONE
                },
                doOnError = {
                    if (it.exception is NoInternetException) {
                        binding.csvDetailFlight.setState(ContentState.ERROR_NETWORK)
                    } else {
                        binding.csvDetailFlight.setState(
                            ContentState.ERROR_GENERAL,
                        )
                    }
                },
                doOnEmpty = {
                    binding.csvDetailFlight.setState(ContentState.EMPTY, desc = "Data not found")
                },
            )
        }
    }

    private fun convertPassengerDates(passengerList: List<BookingPassenger>): List<BookingPassenger> {
        return passengerList.map { passenger ->
            if (passenger.passengerType == "Baby") {
                passenger
            } else {
                passenger.copy(
                    dob = passenger.dob.toConvertDateFormat(),
                    identificationExpired = passenger.identificationExpired?.toConvertDateFormat() ?: passenger.identificationExpired,
                )
            }
        }
    }

    private fun processDateTime(departureTime: String): Pair<String, String> {
        val parts = departureTime.split("T")
        val date = parts[0]
        var times = parts[1].removeSuffix(".000Z")

        if (times.endsWith(":00")) {
            times = times.replace(Regex(":00$"), "")
        }

        return Pair(date, times)
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date as Date)
    }

    private fun calculateDuration(
        departureTime: String,
        arrivalTime: String,
    ): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val departureDateTime = sdf.parse(departureTime)
            val arrivalDateTime = sdf.parse(arrivalTime)

            val diffInMillis = (arrivalDateTime?.time ?: 0) - (departureDateTime?.time ?: 0)

            val hours = diffInMillis / (1000 * 60 * 60)
            val minutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60)

            return "(${hours}h ${minutes}m)"
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    private fun bindView(detail: FlightDetail) {
        detail.let {
            val (departureDate, departureTimes) = processDateTime(it.departureTime)
            val (arrivalDate, arrivalTimes) = processDateTime(it.arrivalTime)
            binding.layoutFlightDetailPrice.tvDeparturePlace.text = it.departureAirport.airportCity
            binding.layoutFlightDetailPrice.tvArrivalPlace.text = it.arrivalAirport.airportCity
            binding.layoutFlightDetailPrice.flightTime.text = calculateDuration(it.departureTime, it.arrivalTime)
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvDepartureTime.text = departureTimes
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvDepartureDate.text = formatDate(departureDate)
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvDepartureAirport.text = "${it.departureAirport.airportName} - ${it.departureTerminal}"
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvAirplane.text = it.airline.airlineName
            binding.layoutFlightDetailPrice.layoutFlightInfo.ivAirplaneLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvCodeAirplane.text = it.flightNumber
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvFlightEntertain.text = it.information
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvArrivalTime.text = arrivalTimes
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvArrivalDate.text = formatDate(arrivalDate)
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvArrivalAirport.text = it.arrivalAirport.airportName
        }
    }

    private fun bindViewReturn(detail: FlightDetail) {
        detail.let {
            val (departureDate, departureTimes) = processDateTime(it.departureTime)
            val (arrivalDate, arrivalTimes) = processDateTime(it.arrivalTime)
            binding.layoutFlightDetailPrice.tvDeparturePlaceReturn.text = it.departureAirport.airportCity
            binding.layoutFlightDetailPrice.tvArrivalPlaceReturn.text = it.arrivalAirport.airportCity
            binding.layoutFlightDetailPrice.flightTimeReturn.text = calculateDuration(it.departureTime, it.arrivalTime)
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvDepartureTime.text = departureTimes
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvDepartureDate.text = formatDate(departureDate)
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvDepartureAirport.text = "${it.departureAirport.airportName} - ${it.departureTerminal}"
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvAirplane.text = it.airline.airlineName
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.ivAirplaneLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvCodeAirplane.text = it.flightNumber
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvFlightEntertain.text = it.information
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvArrivalTime.text = arrivalTimes
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvArrivalDate.text = formatDate(arrivalDate)
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvArrivalAirport.text = it.arrivalAirport.airportName
        }
    }

    private fun openUrl(redirectUrl: String?) {
        val flightId = intent.getStringExtra("flightId")
        if (!redirectUrl.isNullOrBlank()) {
            val intent = Intent(this, WebViewMidtransActivity::class.java)
            intent.putExtra("URL", redirectUrl)
            intent.putExtra("flightId", flightId)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Redirect URL is empty or null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createBooking() {
        val flightId = intent.getStringExtra("flightId")?.toInt()
        val returnFlightId = null
        val fullName = intent.getStringExtra("full_name")
        val phoneNumber = intent.getStringExtra("number_phone")
        val email = intent.getStringExtra("email")
        val familyName = intent.getStringExtra("family_name")
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()
        passengerList = convertPassengerDates(passengerList).toMutableList()
        val orderedBy =
            OrderedBy(
                firstName = fullName ?: "",
                lastName = familyName ?: "",
                phoneNumber = phoneNumber ?: "",
                email = email ?: "",
            )

        if (flightId != null) {
            flightDetailPriceViewModel.doBooking(
                flightId,
                returnFlightId,
                paymentMethod,
                orderedBy,
                passengerList,
            ).observe(this) { result ->
                result.proceedWhen(
                    doOnSuccess = {
                        Toast.makeText(this, "Booking Success ${it.payload}", Toast.LENGTH_SHORT).show()
                        val redirectUrl = result.payload
                        openUrl(redirectUrl)
                    },
                    doOnError = {
                        if (it.exception is ApiErrorException) {
                            val errorBody = it.exception.errorResponse.message.orEmpty()
                            showSnackBarError(errorBody)
                        } else if (it.exception is NoInternetException) {
                            showSnackBarError(getString(R.string.text_no_internet))
                        }
                        Toast.makeText(this, "Booking Error", Toast.LENGTH_SHORT).show()
                    },
                    doOnLoading = {
                    },
                )
            }
        }
    }
}
