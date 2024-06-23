package com.nafi.airseat.presentation.detailflight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.databinding.ActivityDetailFlightBinding
import com.nafi.airseat.presentation.biodata.OrdererBioActivity
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.resultsearchreturn.ResultSearchReturnActivity
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.toCompleteDateFormat
import com.nafi.airseat.utils.toCurrencyFormat
import com.nafi.airseat.utils.toTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
    private var isReturn: Boolean? = null
    private var priceDepart: Int? = null
    private var priceReturn: Int? = null

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

    private fun handleIntent(intent: Intent) {
        if (intent.hasExtra("returnFlight")) {
            val returnFlight = intent.getIntExtra("returnFlight", -1)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val id = intent.getStringExtra("id")
        val price = intent.getIntExtra("price", 0)
        binding.tvTotalPrice.text = price.toLong().toCurrencyFormat()
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)
        Log.d("DetailFlight", "Adults: $adultCount, Children: $childCount, Babies: $babyCount")
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
                isReturn = false
            } else {
                flightDetail?.let {
                    navigateToOrderBio(it.id.toString())
                }
            }
        }
    }

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
                    binding.csvDetailFlight.setState(ContentState.EMPTY, desc = "Data not found")
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
            binding.tvDeparturePlace.text = it.departureAirport.airportCity
            binding.tvArrivalPlace.text = it.arrivalAirport.airportCity
            binding.tvDurationDetail.text = calculateDuration(it.departureTime, it.arrivalTime)
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
            binding.tvDurationDetailReturn.text = calculateDuration(it.departureTime, it.arrivalTime)
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

    private fun navigateToOrderBio(id: String) {
    private fun navigateToOrdererBio(id: String) {
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)
        val price = intent.getIntExtra("price", 0)

        startActivity(
            Intent(this, OrdererBioActivity::class.java).apply {
                putExtra("id", id)
                putExtra("price", price)
                putExtra("airportCityCodeDeparture", airportCityCodeDeparture)
                putExtra("airportCityCodeDestination", airportCityCodeDestination)
                putExtra("seatClassChoose", seatClassChoose)
                putExtra("adultCount", adultCount)
                putExtra("childCount", childCount)
                putExtra("babyCount", babyCount)
                putExtra("idDepart", id)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateBackToResultSearch() {
        val intent = Intent(this, ResultSearchReturnActivity::class.java)
        intent.putExtra("departureAirportId", flightDetail?.arrivalAirportId)
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
        val totalPrice = priceReturn?.let { priceDepart?.plus(it) }
        binding.tvTotalPrice.text = totalPrice?.toLong()?.toCurrencyFormat()
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
}
