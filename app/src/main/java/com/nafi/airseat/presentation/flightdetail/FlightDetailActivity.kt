package com.nafi.airseat.presentation.flightdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.data.model.Passenger
import com.nafi.airseat.data.source.network.model.booking.OrderedBy
import com.nafi.airseat.databinding.ActivityFlightDetailBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.payment.WebViewMidtransActivity
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.toIndonesianFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class FlightDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRAS_PASSENGER_LIST = "EXTRAS_PASSENGER_LIST"

        fun startActivity(
            context: Context,
            passenger: ArrayList<Passenger>,
        ) {
            val intent = Intent(context, FlightDetailActivity::class.java)
            intent.putExtra(EXTRAS_PASSENGER_LIST, passenger)
            context.startActivity(intent)
        }
    }

    private val binding: ActivityFlightDetailBinding by lazy {
        ActivityFlightDetailBinding.inflate(layoutInflater)
    }

    private val flightDetailPriceViewModel: FlightDetailPriceViewModel by viewModel()
    private val paymentMethod = "snap"
    private var flightDetail: FlightDetail? = null
    private lateinit var passengerList: MutableList<Passenger>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val tax = intent.getDoubleExtra("tax", 0.0)
        val promo = intent.getDoubleExtra("promo", 0.0)
        val price = intent.getIntExtra("price", 0)
        val flightId = intent.getStringExtra("flightId")
        val fullName = intent.getStringExtra("full_name")
        val phoneNumber = intent.getStringExtra("number_phone")
        val email = intent.getStringExtra("email")
        val familyName = intent.getStringExtra("family_name")
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()
        proceedDetailTicket(flightId.toString())
        bindPrice()

        passengerList.forEach { passenger ->
            Log.d("FlightDetailPriceActivity", "Passenger: $passenger")
        }

        Log.d("OrderedBy", "Ordered Bio: $fullName, $familyName, $phoneNumber, $email")

        passengerList = intent.getParcelableArrayListExtra(EXTRAS_PASSENGER_LIST) ?: arrayListOf()

        binding.cvSectionCheckout.btnPayment.setOnClickListener {
            openUrl()
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
            binding.layoutPrice.llPriceAdults.visibility = View.VISIBLE
            binding.layoutPrice.tvAdults.text = "$adults Adult${if (adults > 1) "s" else ""}"
            val totalPriceAdult = price * adults
            binding.layoutPrice.tvAdultPrice.text = totalPriceAdult.toIndonesianFormat()
            totalPrice += totalPriceAdult
        } else {
            binding.layoutPrice.llPriceAdults.visibility = View.GONE
        }

        if (child > 0) {
            binding.layoutPrice.llPriceChild.visibility = View.VISIBLE
            binding.layoutPrice.tvChild.text = "$child Child${if (child > 1) "ren" else ""}"
            val totalPriceChild = price * child
            binding.layoutPrice.tvChildPrice.text = totalPriceChild.toIndonesianFormat()
            totalPrice += totalPriceChild
        } else {
            binding.layoutPrice.llPriceChild.visibility = View.GONE
        }

        if (baby > 0) {
            binding.layoutPrice.llPriceBaby.visibility = View.VISIBLE
            binding.layoutPrice.tvBaby.text = "$baby Baby${if (baby > 1) "ies" else ""}"
            binding.layoutPrice.tvBabyPrice.text = priceBaby.toIndonesianFormat()
        } else {
            binding.layoutPrice.llPriceBaby.visibility = View.GONE
        }
        binding.cvSectionCheckout.totalPrice.text = totalPrice.toIndonesianFormat()
    }

    private fun proceedDetailTicket(flightId: String) {
        flightDetailPriceViewModel.getDetailFlight(flightId).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.csvDetailFlight.setState(ContentState.SUCCESS)
                    binding.layoutFlightInfo.root.visibility = View.VISIBLE
                    it.payload?.let { detail ->
                        flightDetail = detail
                        bindView(detail)
                    }
                },
                doOnLoading = {
                    binding.csvDetailFlight.setState(ContentState.LOADING)
                    binding.layoutFlightInfo.root.visibility = View.GONE
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

    fun calculateDuration(
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
            binding.tvDeparturePlace.text = it.departureAirport.airportCity
            binding.tvArrivalPlace.text = it.arrivalAirport.airportCity
            binding.flightTime.text = calculateDuration(it.departureTime, it.arrivalTime)
            binding.layoutFlightInfo.tvDepartureTime.text = departureTimes
            binding.layoutFlightInfo.tvDepartureDate.text = formatDate(departureDate)
            binding.layoutFlightInfo.tvDepartureAirport.text = "${it.departureAirport.airportName} - ${it.departureTerminal}"
            binding.layoutFlightInfo.tvAirplane.text = it.airline.airlineName
            binding.layoutFlightInfo.ivAirplaneLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutFlightInfo.tvCodeAirplane.text = it.flightNumber
            binding.layoutFlightInfo.tvFlightEntertain.text = it.information
            binding.layoutFlightInfo.tvArrivalTime.text = arrivalTimes
            binding.layoutFlightInfo.tvArrivalDate.text = formatDate(arrivalDate)
            binding.layoutFlightInfo.tvArrivalAirport.text = it.arrivalAirport.airportName
        }
    }

    private fun openUrl() {
        val intent = Intent(this, WebViewMidtransActivity::class.java)
        intent.putExtra(
            "URL",
            "https://app.sandbox.midtrans.com/snap/v4/redirection/",
        )
        startActivity(intent)
    }

    private suspend fun proceedBooking() {
        val fullName = intent.getStringExtra("full_name")
        val phoneNumber = intent.getStringExtra("number_phone")
        val email = intent.getStringExtra("email")
        val familyName = intent.getStringExtra("family_name")

        val orderedBy =
            OrderedBy(
                firstName = fullName ?: "",
                lastName = familyName ?: "",
                phoneNumber = phoneNumber ?: "",
                email = email ?: "",
            )

        Log.d("OrderedBy", "Ordered Bio: $fullName, $familyName, $phoneNumber, $email")

        val passengers =
            passengerList.map {
                Passenger(
                    firstName = it.firstName,
                    familyName = it.familyName,
                    title = it.title,
                    dob = it.dob,
                    nationality = it.nationality,
                    identificationType = it.identificationType,
                    identificationNumber = it.identificationNumber,
                    identificationCountry = it.identificationCountry,
                    identificationExpired = it.identificationExpired,
                    seatDeparture = it.seatDeparture,
                )
            }

        /*try {
            val response =
                flightDetailPriceViewModel.doBooking(
                    flightId = flightId,
                    paymentMethod = paymentMethod,
                    discountId = discountId,
                    orderedBy = orderedBy,
                    passenger = passengers,
                )

            if (response.status == "success") {
                // Booking berhasil
                Log.d("FlightDetailActivity", "Booking successful: ${response.message}")
                // Tampilkan pesan atau lakukan tindakan setelah booking berhasil
            } else {
                // Booking gagal
                Log.d("FlightDetailActivity", "Booking failed: ${response.message}")
                // Tampilkan pesan atau lakukan tindakan setelah booking gagal
            }
        } catch (e: Exception) {
            // Error saat melakukan booking
            Log.e("FlightDetailActivity", "Error during booking: ${e.message}")
            // Tampilkan pesan atau lakukan tindakan pemulihan jika diperlukan
        }*/
    }
}
