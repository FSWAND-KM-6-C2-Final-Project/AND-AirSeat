package com.c2.airseat.presentation.detailhistory

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import coil.load
import com.c2.airseat.R
import com.c2.airseat.data.model.BookingDetailHistory
import com.c2.airseat.data.model.History
import com.c2.airseat.databinding.ActivityDetailHistoryBinding
import com.c2.airseat.presentation.detailhistory.adapter.PassengerAdapter
import com.c2.airseat.presentation.payment.WebViewMidtransActivity
import com.c2.airseat.utils.capitalizeFirstLetter
import com.c2.airseat.utils.showSnackBarError
import com.c2.airseat.utils.toCompleteDateFormat
import com.c2.airseat.utils.toCurrencyFormat
import com.c2.airseat.utils.toTimeClock
import com.c2.airseat.utils.toTimeFormat

class DetailHistoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailHistoryBinding by lazy {
        ActivityDetailHistoryBinding.inflate(layoutInflater)
    }
    private val passengerDepartAdapter: PassengerAdapter by lazy {
        PassengerAdapter()
    }
    private val passengerReturnAdapter: PassengerAdapter by lazy {
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
        binding.layoutFlightInfo.layoutOrderInfo.rvPassengers.adapter =
            this@DetailHistoryActivity.passengerDepartAdapter
        binding.layoutFlightInfo.layoutReturnOrderInfo.rvPassengers.adapter =
            this@DetailHistoryActivity.passengerReturnAdapter
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
            binding.layoutFlightInfo.layoutOrderInfo.tvArrivalTime.text =
                it.flight.arrivalTime.toTimeClock()
            binding.layoutFlightInfo.layoutOrderInfo.tvArrivalDate.text =
                it.flight.arrivalTime.toCompleteDateFormat()
            binding.layoutFlightInfo.layoutOrderInfo.tvArrivalAirport.text =
                it.flight.arrivalAirport.airportName
            binding.layoutFlightInfo.tvTotalPriceDetail.text = it.totalAmount.toCurrencyFormat()

            setAdapterData(it)
            setViewReturnFlight(it)
            val passengerSummary = aggregatePassengerData(it.bookingDetail)
            bindDataToLayout(passengerSummary)
            setButtonDetailHistory(it.bookingStatus)
            continuePayment(it.paymentUrl)
        }
    }

    private fun setAdapterData(data: History) {
        val bookingDetailList = data.bookingDetail
        val halfSize = bookingDetailList.size / 2
        val firstHalfBookingDetailList = bookingDetailList.subList(0, halfSize)
        val secondHalfBookingDetailList =
            bookingDetailList.subList(halfSize, bookingDetailList.size)
        if (data.returnFlight?.flightNumber?.length == null) {
            passengerDepartAdapter.insertData(data.bookingDetail)
            Toast.makeText(this, "${data.returnFlight?.flightNumber?.length}", Toast.LENGTH_SHORT)
                .show()
        } else {
            passengerDepartAdapter.insertData(secondHalfBookingDetailList)
            passengerReturnAdapter.insertData(firstHalfBookingDetailList)
        }
    }

    private fun setViewReturnFlight(data: History) {
        data.returnFlight.let {
            if (data.returnFlight?.flightNumber?.length == null) {
                binding.layoutFlightInfo.layoutReturnOrderInfo.root.isVisible = false
            } else {
                binding.layoutFlightInfo.layoutReturnOrderInfo.root.isVisible = true
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvDepartureTime.text =
                    data.returnFlight.departureTime.toTimeFormat()
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvDepartureDate.text =
                    data.returnFlight.departureTime.toCompleteDateFormat()
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvDepartureAirport.text =
                    getString(
                        R.string.text_detail_history_airport_terminal_departure,
                        data.returnFlight.departureAirport.airportName,
                        data.returnFlight.departureTerminal,
                    )
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvAirplane.text =
                    data.returnFlight.airline.airlineName
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvCodeAirplane.text =
                    data.returnFlight.flightNumber
                binding.layoutFlightInfo.layoutReturnOrderInfo.ivAirplaneLogo.load(data.returnFlight.airline.airlinePicture) {
                    crossfade(true)
                }
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvArrivalTime.text =
                    data.returnFlight.arrivalTime.toTimeClock()
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvArrivalDate.text =
                    data.returnFlight.arrivalTime.toCompleteDateFormat()
                binding.layoutFlightInfo.layoutReturnOrderInfo.tvArrivalAirport.text =
                    data.returnFlight.arrivalAirport.airportName
            }
        }
    }

    private fun setButtonDetailHistory(status: String) {
        if (status == "unpaid") {
            binding.btnHome.isVisible = false
            binding.btnContinuePayment.isVisible = true
        } else {
            binding.btnHome.isVisible = true
            binding.btnContinuePayment.isVisible = false
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

        binding.btnHome.setOnClickListener {
            onBackPressed()
        }
    }

    private fun continuePayment(redirectUrl: String?) {
        binding.btnContinuePayment.setOnClickListener {
            if (!redirectUrl.isNullOrBlank()) {
                val intent = Intent(this, WebViewMidtransActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("URL", redirectUrl)
                startActivity(intent)
            } else {
                showSnackBarError(getString(R.string.text_failed_to_continue_payment))
            }
        }
    }
}
