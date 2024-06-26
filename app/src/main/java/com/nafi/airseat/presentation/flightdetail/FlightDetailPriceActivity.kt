package com.nafi.airseat.presentation.flightdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.nafi.airseat.utils.showSnackBarSuccess
import com.nafi.airseat.utils.toCompleteDateFormat
import com.nafi.airseat.utils.toConvertDateFormat
import com.nafi.airseat.utils.toCurrencyFormat
import com.nafi.airseat.utils.toTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        val flightId = intent.getStringExtra("idDepart")
        val idReturn = intent.getIntExtra("idReturn", 0)
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

        binding.cvSectionCheckout.btnPayment.setOnClickListener {
            createBooking()
        }
        binding.ibBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bindPrice() {
        val price = intent.getIntExtra("price", 0).toLong()
        val priceBaby = 0.0
        val adults = intent.getIntExtra("adults", 0)
        val baby = intent.getIntExtra("baby", 0)
        val child = intent.getIntExtra("child", 0)
        var totalPrice = 0.0

        if (adults > 0) {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceAdults.visibility = View.VISIBLE
            binding.layoutFlightDetailPrice.layoutPrice.tvAdults.text =
                getString(R.string.text_adult_count, adults)
            val totalPriceAdult = price * adults
            binding.layoutFlightDetailPrice.layoutPrice.tvAdultPrice.text =
                totalPriceAdult.toCurrencyFormat()
            totalPrice += totalPriceAdult
        } else {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceAdults.visibility = View.GONE
        }

        if (child > 0) {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceChild.visibility = View.VISIBLE
            binding.layoutFlightDetailPrice.layoutPrice.tvChild.text =
                getString(R.string.text_child_count, child)
            val totalPriceChild = price * child
            binding.layoutFlightDetailPrice.layoutPrice.tvChildPrice.text =
                totalPriceChild.toCurrencyFormat()
            totalPrice += totalPriceChild
        } else {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceChild.visibility = View.GONE
        }

        if (baby > 0) {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceBaby.visibility = View.VISIBLE
            binding.layoutFlightDetailPrice.layoutPrice.tvBaby.text =
                getString(R.string.text_baby_count, baby)
            binding.layoutFlightDetailPrice.layoutPrice.tvBabyPrice.text =
                priceBaby.toLong().toCurrencyFormat()
        } else {
            binding.layoutFlightDetailPrice.layoutPrice.llPriceBaby.visibility = View.GONE
        }
        binding.cvSectionCheckout.totalPrice.text = totalPrice.toLong().toCurrencyFormat()
    }

    private fun proceedDetailTicket(flightId: String) {
        flightDetailPriceViewModel.getDetailFlight(flightId).observe(this) { it ->
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
                    binding.csvDetailFlight.setState(
                        ContentState.EMPTY,
                        desc = getString(R.string.text_data_not_found),
                    )
                },
            )
        }
    }

    private fun proceedDetailReturnTicket(idReturn: String) {
        flightDetailPriceViewModel.getDetailFlight(idReturn).observe(this) { it ->
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
                    binding.csvDetailFlight.setState(
                        ContentState.EMPTY,
                        desc = getString(R.string.text_data_not_found),
                    )
                },
            )
        }
    }

    private fun convertPassengerDates(passengerList: List<BookingPassenger>): List<BookingPassenger> {
        return passengerList.map { passenger ->
            if (passenger.identificationType == "ktp") {
                passenger.copy(
                    dob = passenger.dob.toConvertDateFormat(),
                )
            } else {
                passenger.copy(
                    dob = passenger.dob.toConvertDateFormat(),
                    identificationExpired =
                        passenger.identificationExpired?.toConvertDateFormat()
                            ?: passenger.identificationExpired,
                )
            }
        }
    }

    private fun bindView(detail: FlightDetail) {
        detail.let {
            binding.layoutFlightDetailPrice.tvDeparturePlace.text = it.departureAirport.airportCity
            binding.layoutFlightDetailPrice.tvArrivalPlace.text = it.arrivalAirport.airportCity
            binding.layoutFlightDetailPrice.flightTime.text =
                getString(R.string.text_duration_estimation, it.duration)
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvDepartureTime.text =
                it.departureTime.toTimeFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvDepartureDate.text =
                it.departureTime.toConvertDateFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvDepartureAirport.text =
                getString(
                    R.string.text_detail_departure_ariport,
                    it.departureAirport.airportName,
                    it.departureTerminal,
                )
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvAirplane.text =
                it.airline.airlineName
            binding.layoutFlightDetailPrice.layoutFlightInfo.ivAirplaneLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvCodeAirplane.text = it.flightNumber
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvFlightEntertain.text = it.information
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvArrivalTime.text =
                it.arrivalTime.toTimeFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvArrivalDate.text =
                it.arrivalTime.toCompleteDateFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfo.tvArrivalAirport.text =
                it.arrivalAirport.airportName
        }
    }

    private fun bindViewReturn(detail: FlightDetail) {
        detail.let {
            binding.layoutFlightDetailPrice.tvDeparturePlaceReturn.text =
                it.departureAirport.airportCity
            binding.layoutFlightDetailPrice.tvArrivalPlaceReturn.text =
                it.arrivalAirport.airportCity
            binding.layoutFlightDetailPrice.flightTimeReturn.text =
                getString(R.string.text_duration_estimation, it.duration)
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvDepartureTime.text =
                it.departureTime.toTimeFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvDepartureDate.text =
                it.departureTime.toCompleteDateFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvDepartureAirport.text =
                getString(
                    R.string.text_detail_departure_ariport,
                    it.departureAirport.airportName,
                    it.departureTerminal,
                )
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvAirplane.text =
                it.airline.airlineName
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.ivAirplaneLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvCodeAirplane.text =
                it.flightNumber
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvFlightEntertain.text =
                it.information
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvArrivalTime.text =
                it.arrivalTime.toTimeFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvArrivalDate.text =
                it.arrivalTime.toCompleteDateFormat()
            binding.layoutFlightDetailPrice.layoutFlightInfoReturn.tvArrivalAirport.text =
                it.arrivalAirport.airportName
        }
    }

    private fun openUrl(redirectUrl: String?) {
        val flightId = intent.getStringExtra("idDepart")
        if (!redirectUrl.isNullOrBlank()) {
            val intent = Intent(this, WebViewMidtransActivity::class.java)
            intent.putExtra("URL", redirectUrl)
            intent.putExtra("flightId", flightId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            showSnackBarError(getString(R.string.text_failed_order))
        }
    }

    private fun createBooking() {
        val flightId = intent.getStringExtra("idDepart")?.toInt()
        val returnFlightId = intent.getIntExtra("idReturn", 0)
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
                        binding.cvSectionCheckout.pbLoading.isVisible = false
                        binding.cvSectionCheckout.btnPayment.isVisible = true
                        showSnackBarSuccess(getString(R.string.text_booking_success))
                        val redirectUrl = result.payload
                        openUrl(redirectUrl)
                    },
                    doOnError = {
                        binding.cvSectionCheckout.pbLoading.isVisible = false
                        binding.cvSectionCheckout.btnPayment.isVisible = true
                        if (it.exception is ApiErrorException) {
                            val errorBody = it.exception.errorResponse.message.orEmpty()
                            showSnackBarError(errorBody)
                        } else if (it.exception is NoInternetException) {
                            showSnackBarError(getString(R.string.text_no_internet))
                        }
                        showSnackBarError(getString(R.string.text_booking_error))
                    },
                    doOnLoading = {
                        binding.cvSectionCheckout.pbLoading.isVisible = true
                        binding.cvSectionCheckout.btnPayment.isVisible = false
                    },
                )
            }
        }
    }
}
