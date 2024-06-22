package com.nafi.airseat.presentation.detailflight

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.databinding.ActivityDetailFlightBinding
import com.nafi.airseat.presentation.biodata.OrdererBioActivity
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.utils.NoInternetException
import com.nafi.airseat.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val id = intent.getStringExtra("id")
        val price = intent.getIntExtra("price", 0)
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)
        Log.d("DetailFlight", "Adults: $adultCount, Children: $childCount, Babies: $babyCount")
        binding.tvTotalPrice.text = price.toString()

        proceedDetailTicket(id.toString())
        binding.layoutHeader.btnBackHome.setOnClickListener {
            finish()
        }
        binding.btnSave.setOnClickListener {
            flightDetail?.let {
                navigateToOrdererBio(it.id.toString())
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
                        // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    }
                },
                doOnLoading = {
                    binding.csvDetailFlight.setState(ContentState.LOADING)
                    binding.layoutDetail.root.visibility = View.GONE
                    // Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    // Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
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
                    // Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
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
            binding.tvDurationDetail.text = calculateDuration(it.departureTime, it.arrivalTime)
            binding.layoutDetail.tvDepartureTime.text = departureTimes
            binding.layoutDetail.tvDepartureDate.text = formatDate(departureDate)
            binding.layoutDetail.tvDepartureAirportDetail.text = it.departureAirport.airportName
            binding.layoutDetail.tvDeparturePlace.text = it.departureTerminal
            binding.layoutDetail.imgAirlineLogo.load(it.airline.airlinePicture) {
                crossfade(true)
            }
            binding.layoutDetail.tvAirlineName.text = it.airline.airlineName
            binding.layoutDetail.tvAirlineCode.text = it.flightNumber
            binding.layoutDetail.tvFlightType.text = it.information
            binding.layoutDetail.tvArrivalTime.text = arrivalTimes
            binding.layoutDetail.tvArrivalDate.text = formatDate(arrivalDate)
            binding.layoutDetail.tvArrivalPlace.text = it.arrivalAirport.airportName
            // binding.tvTotalPrice.text = it.pricePremiumEconomy
        }
    }

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
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
