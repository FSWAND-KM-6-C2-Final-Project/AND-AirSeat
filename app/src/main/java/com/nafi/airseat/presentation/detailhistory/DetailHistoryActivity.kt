package com.nafi.airseat.presentation.detailhistory

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.nafi.airseat.R
import com.nafi.airseat.data.model.BookingDetailHistory
import com.nafi.airseat.data.model.History
import com.nafi.airseat.databinding.ActivityDetailHistoryBinding
import com.nafi.airseat.presentation.detailhistory.adapter.PassengerAdapter
import com.nafi.airseat.utils.capitalizeFirstLetter
import com.nafi.airseat.utils.toCompleteDateFormat
import com.nafi.airseat.utils.toCurrencyFormat
import com.nafi.airseat.utils.toTimeClock
import com.nafi.airseat.utils.toTimeFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailHistoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailHistoryBinding by lazy {
        ActivityDetailHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailHistoryViewModel by viewModel()
    private val adapter: PassengerAdapter by lazy {
        PassengerAdapter()
    }

    companion object {
        const val EXTRAS_DETAIL_DATA = "EXTRAS_DETAIL_DATA"

        fun startActivity(
            context: Context,
            menu: History,
        ) {
            val intent = Intent(context, DetailHistoryActivity::class.java)
            intent.putExtra(EXTRAS_DETAIL_DATA, menu)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setAdapter()
        getIntentData()
        setOnClickListener()
    }

    private fun setAdapter() {
        binding.layoutFlightInfo.layoutOrderInfo.rvPassengers.adapter = adapter
    }

    private fun getIntentData() {
        intent.extras?.getParcelable<History>(EXTRAS_DETAIL_DATA)?.let {
            setBackgroundStatus(it.bookingStatus, binding)
            binding.layoutFlightInfo.tvStatusDetailHistory.text =
                it.bookingStatus.capitalizeFirstLetter()
            binding.layoutFlightInfo.layoutOrderInfo.tvBookingCode.text = it.bookingCode
            binding.layoutFlightInfo.layoutOrderInfo.tvDepartureTime.text =
                it.flight.departureTime.toTimeFormat()
            binding.layoutFlightInfo.layoutOrderInfo.tvDepartureDate.text =
                it.flight.departureTime.toCompleteDateFormat()
            binding.layoutFlightInfo.layoutOrderInfo.tvDepartureAirport.text =
                getString(
                    R.string.text_detail_history_airport_terminal_departure,
                    it.flight.departureAirport.airportName,
                    it.flight.departureTerminal,
                )
            binding.layoutFlightInfo.layoutOrderInfo.tvAirplane.text = it.flight.airline.airlineName
            binding.layoutFlightInfo.layoutOrderInfo.tvCodeAirplane.text = it.flight.flightNumber
            binding.layoutFlightInfo.layoutOrderInfo.ivAirplaneLogo.load(it.flight.airline.airlinePicture) {
                crossfade(true)
            }
            adapter.insertData(it.bookingDetail)
            binding.layoutFlightInfo.layoutOrderInfo.tvArrivalTime.text =
                it.flight.arrivalTime.toTimeClock()
            binding.layoutFlightInfo.layoutOrderInfo.tvArrivalDate.text =
                it.flight.arrivalTime.toCompleteDateFormat()
            binding.layoutFlightInfo.layoutOrderInfo.tvArrivalAirport.text =
                it.flight.arrivalAirport.airportName

            val passengerSummary = aggregatePassengerData(it.bookingDetail)
            bindDataToLayout(passengerSummary)

            binding.layoutFlightInfo.tvTotalPriceDetail.text = it.totalAmount.toCurrencyFormat()
        }
    }

    private fun setBackgroundStatus(
        status: String,
        binding: ActivityDetailHistoryBinding,
    ) {
        if (status == "issued") {
            binding.layoutFlightInfo.cvStatus.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#73CA5C"))
        } else if (status == "unpaid") {
            binding.layoutFlightInfo.cvStatus.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FF0000"))
        }
    }

    private fun aggregatePassengerData(bookingDetails: List<BookingDetailHistory>): Map<String, Pair<Int, Int>> {
        val passengerSummary = mutableMapOf<String, Pair<Int, Int>>()

        for (detail in bookingDetails) {
            val passengerType = detail.passenger.passengerType
            val price = detail.price

            val currentCountPrice = passengerSummary.getOrDefault(passengerType, Pair(0, 0))
            val newCount = currentCountPrice.first + 1
            val newPrice = currentCountPrice.second + price

            passengerSummary[passengerType] = Pair(newCount, newPrice)
        }
        return passengerSummary
    }

    private fun bindDataToLayout(passengerSummary: Map<String, Pair<Int, Int>>) {
        passengerSummary.forEach { (passengerType, countPrice) ->
            val count = countPrice.first
            val price = countPrice.second

            when (passengerType) {
                "adult" -> {
                    binding.layoutFlightInfo.layoutPrice.llPriceAdults.visibility = View.VISIBLE
                    binding.layoutFlightInfo.layoutPrice.tvAdults.text =
                        getString(R.string.text_detail_history_adults, count.toString())
                    binding.layoutFlightInfo.layoutPrice.tvAdultPrice.text =
                        price.toLong().toCurrencyFormat()
                }

                "children" -> {
                    binding.layoutFlightInfo.layoutPrice.llPriceChild.visibility = View.VISIBLE
                    binding.layoutFlightInfo.layoutPrice.tvChild.text =
                        getString(R.string.text_detail_history_child, count.toString())
                    binding.layoutFlightInfo.layoutPrice.tvChildPrice.text =
                        price.toLong().toCurrencyFormat()
                }

                "infant" -> {
                    binding.layoutFlightInfo.layoutPrice.llPriceBaby.visibility = View.VISIBLE
                    binding.layoutFlightInfo.layoutPrice.tvBaby.text =
                        getString(R.string.text_detail_history_baby, count.toString())
                    binding.layoutFlightInfo.layoutPrice.tvBabyPrice.text =
                        price.toLong().toCurrencyFormat()
                }

                else -> {
                }
            }
        }
    }

    private fun setOnClickListener() {
        binding.ibBackDetailHistory.setOnClickListener {
            onBackPressed()
        }
    }
}
