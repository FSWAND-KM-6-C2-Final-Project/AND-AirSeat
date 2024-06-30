package com.c2.airseat.presentation.detailflight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import com.c2.airseat.R
import com.c2.airseat.data.model.FlightDetail
import com.c2.airseat.databinding.ActivityDetailFlightBinding
import com.c2.airseat.presentation.biodata.OrdererBioActivity
import com.c2.airseat.presentation.bottomsheet.ProtectedLoginBottomSheet
import com.c2.airseat.presentation.common.views.ContentState
import com.c2.airseat.presentation.resultsearchreturn.ResultSearchReturnActivity
import com.c2.airseat.utils.NoInternetException
import com.c2.airseat.utils.proceedWhen
import com.c2.airseat.utils.toCompleteDateFormat
import com.c2.airseat.utils.toCurrencyFormat
import com.c2.airseat.utils.toTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailFlightActivity : AppCompatActivity() {
    private val binding: ActivityDetailFlightBinding by lazy {
        ActivityDetailFlightBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailFlightViewModel by viewModel {
        parametersOf(intent.extras)
    }
    private var flightDetail: FlightDetail? = null
    private var endDate: String? = null
    private var passengerCount: String? = null
    private var seatClassChoose: String? = null
    private var idDepart: String? = null
    private var idFlightReturn: Int? = null
    private var isReturn: Boolean? = null
    private var priceDepart: Int? = null
    private var priceReturn: Int? = null
    private var totalPrice: Int = 0

    companion object {
        const val EXTRAS_ITEM = "EXTRAS_ITEM"

        fun startActivity(
            context: Context,
            id: String,
        ) {
            val intent = Intent(context, DetailFlightActivity::class.java)
            intent.putExtra(EXTRAS_ITEM, id)
            context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val id = intent.getStringExtra("id")
        val returnFlight = intent.getIntExtra("returnFlight", 0)
        if (returnFlight != 0) {
            val idReturn = intent.getIntExtra("returnFlight", 0)
            binding.llFlightReturnTicket.isVisible = true
            binding.layoutDetailReturn.root.isVisible = true
            proceedDetailTicket(id.orEmpty())
            proceedDetailTicketReturn(idReturn.toString())
        } else {
            proceedDetailTicket(id.orEmpty())
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val id = intent.getStringExtra("id")
        idFlightReturn = intent.getIntExtra("returnFlight", 0)
        val price = intent.getIntExtra("price", 0)
        binding.tvTotalPrice.text = price.toLong().toCurrencyFormat()
        binding.tvTotalPrice.text = price.toString()
        proceedDetailTicket(id.toString())
        idDepart = id.orEmpty()
        priceDepart = intent.getIntExtra("priceDepart", 0)
        priceReturn = intent.getIntExtra("priceReturn", 0)
        isReturn = intent.getBooleanExtra("isReturnFlight", false)
        binding.layoutHeader.btnBackHome.setOnClickListener {
            onBackPressed()
        }
        endDate = intent.getStringExtra("endDate")
        passengerCount = intent.getStringExtra("passengerCount")
        seatClassChoose = intent.getStringExtra("seatClassChoose")
        totalPrice()
        handleIntent(intent)
        binding.btnSave.setOnClickListener {
            if (isReturn as Boolean) {
                navigateBackToResultSearch()
            } else {
                handleLogin()
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.hasExtra("returnFlight")) {
            val returnFlight = intent.getIntExtra("returnFlight", -1)
            idFlightReturn = returnFlight
            isReturn = false
            if (returnFlight != 0) {
                val idReturn = intent.getIntExtra("returnFlight", 0)
                binding.llFlightReturnTicket.isVisible = true
                binding.layoutDetailReturn.root.isVisible = true
                proceedDetailTicket(idDepart.orEmpty())
                proceedDetailTicketReturn(idReturn.toString())
            } else {
                proceedDetailTicket(idDepart.orEmpty())
            }
        }
    }

    private fun proceedDetailTicket(id: String) {
        viewModel.getDetailFlight(id).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.csvDetailFlight.setState(ContentState.SUCCESS)
                    binding.layoutDetail.root.visibility = View.VISIBLE
                    it.payload?.let { detail ->
                        flightDetail = detail
                        bindView(detail)
                        getPriceDepart(detail)
                        totalPrice()
                    }
                },
                doOnLoading = {
                    binding.csvDetailFlight.setState(ContentState.LOADING)
                    binding.layoutDetail.root.visibility = View.GONE
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
                    binding.csvDetailFlight.setState(ContentState.EMPTY, desc = "Data is Empty")
                },
            )
        }
    }

    private fun proceedDetailTicketReturn(id: String) {
        viewModel.getDetailFlight(id).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.csvDetailFlight.setState(ContentState.SUCCESS)
                    binding.layoutDetailReturn.root.visibility = View.VISIBLE
                    it.payload?.let { detail ->
                        flightDetail = detail
                        bindViewReturn(detail)
                        getPriceReturn(detail)
                        totalPrice()
                    }
                },
                doOnLoading = {
                    binding.csvDetailFlight.setState(ContentState.LOADING)
                    binding.layoutDetailReturn.root.visibility = View.GONE
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

    private fun bindView(detail: FlightDetail) {
        detail.let {
            binding.tvDeparturePlace.text = it.departureAirport.airportCity
            binding.tvArrivalPlace.text = it.arrivalAirport.airportCity
            binding.tvDurationDetail.text = getString(R.string.text_duration_estimation, it.duration)
            binding.layoutDetail.tvDepartureTime.text = it.departureTime.toTimeFormat()
            binding.layoutDetail.tvDepartureDate.text = it.departureTime.toCompleteDateFormat()
            binding.layoutDetail.tvDepartureAirportDetail.text = it.departureAirport.airportName
            binding.layoutDetail.tvDeparturePlace.text = it.departureTerminal
            binding.layoutDetail.imgAirlineLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutDetail.tvAirlineName.text = it.airline.airlineName
            binding.layoutDetail.tvAirlineCode.text = it.flightNumber
            binding.layoutDetail.tvFlightType.text = it.information
            binding.layoutDetail.tvArrivalTime.text = it.arrivalTime.toTimeFormat()
            binding.layoutDetail.tvArrivalDate.text = it.arrivalTime.toCompleteDateFormat()
            binding.layoutDetail.tvArrivalPlace.text = it.arrivalAirport.airportName
        }
    }

    private fun bindViewReturn(detail: FlightDetail) {
        detail.let {
            binding.tvDeparturePlaceReturn.text = it.departureAirport.airportCity
            binding.tvArrivalPlaceReturn.text = it.arrivalAirport.airportCity
            binding.tvDurationDetailReturn.text = getString(R.string.text_duration_estimation, it.duration)
            binding.layoutDetailReturn.tvDepartureTime.text = it.departureTime.toTimeFormat()
            binding.layoutDetailReturn.tvDepartureDate.text = it.departureTime.toCompleteDateFormat()
            binding.layoutDetailReturn.tvDepartureAirportDetail.text = it.departureAirport.airportName
            binding.layoutDetailReturn.tvDeparturePlace.text = it.departureTerminal
            binding.layoutDetailReturn.imgAirlineLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutDetailReturn.tvAirlineName.text = it.airline.airlineName
            binding.layoutDetailReturn.tvAirlineCode.text = it.flightNumber
            binding.layoutDetailReturn.tvFlightType.text = it.information
            binding.layoutDetailReturn.tvArrivalTime.text = it.arrivalTime.toTimeFormat()
            binding.layoutDetailReturn.tvArrivalDate.text = it.arrivalTime.toCompleteDateFormat()
            binding.layoutDetailReturn.tvArrivalPlace.text = it.arrivalAirport.airportName
        }
    }

    private fun navigateToOrderBio() {
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)

        startActivity(
            Intent(this, OrdererBioActivity::class.java).apply {
                putExtra("id", idDepart)
                putExtra("price", totalPrice)
                putExtra("airportCityCodeDeparture", airportCityCodeDeparture)
                putExtra("airportCityCodeDestination", airportCityCodeDestination)
                putExtra("seatClassChoose", seatClassChoose)
                putExtra("adultCount", adultCount)
                putExtra("childCount", childCount)
                putExtra("babyCount", babyCount)
                putExtra("idDepart", idDepart)
                putExtra("idFlightReturn", idFlightReturn)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateBackToResultSearch() {
        val intent = Intent(this, ResultSearchReturnActivity::class.java)
        intent.putExtra("departAirportId", flightDetail?.arrivalAirportId)
        intent.putExtra("destinationAirportId", flightDetail?.departureAirportId)
        intent.putExtra("airportCityCodeDeparture", flightDetail?.arrivalAirport?.airportCityCode)
        intent.putExtra("airportCityCodeDestination", flightDetail?.departureAirport?.airportCityCode)
        intent.putExtra("startDate", endDate)
        intent.putExtra("selectedDepart", endDate)
        intent.putExtra("seatClassChoose", seatClassChoose)
        intent.putExtra("passengerCount", passengerCount)
        startActivity(intent)
    }

    private fun totalPrice() {
        totalPrice = priceDepart?.plus(priceReturn ?: 0) ?: 0
        binding.tvTotalPrice.text = totalPrice.toLong().toCurrencyFormat()
    }

    private fun getPriceDepart(data: FlightDetail) {
        when (seatClassChoose) {
            "Economy" -> {
                priceDepart = data.priceEconomy.toInt()
            }

            "Premium Economy" -> {
                priceDepart = data.pricePremiumEconomy.toInt()
            }

            "Business" -> {
                priceDepart = data.priceBusiness.toInt()
            }

            "First Class" -> {
                priceDepart = data.priceFirstClass.toInt()
            }
        }
    }

    private fun getPriceReturn(data: FlightDetail) {
        when (seatClassChoose) {
            "Economy" -> {
                priceReturn = data.priceEconomy.toInt()
            }

            "Premium Economy" -> {
                priceReturn = data.pricePremiumEconomy.toInt()
            }

            "Business" -> {
                priceReturn = data.priceBusiness.toInt()
            }

            "First Class" -> {
                priceReturn = data.priceFirstClass.toInt()
            }
        }
    }

    private fun handleLogin() {
        viewModel.getIsLogin().observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    navigateToOrderBio()
                },
                doOnError = {
                    val dialog = ProtectedLoginBottomSheet()
                    dialog.show(supportFragmentManager, dialog.tag)
                },
            )
        }
    }
}
