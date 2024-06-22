package com.nafi.airseat.presentation.seatbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.data.source.network.model.booking.BookingPassenger
import com.nafi.airseat.data.source.network.model.booking.SeatPassenger
import com.nafi.airseat.databinding.ActivitySeatBookBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.flightdetail.FlightDetailPriceActivity
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.seatbook.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeatBookActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView

    private val seatViewModel: SeatViewModel by viewModel()

    private val binding: ActivitySeatBookBinding by lazy {
        ActivitySeatBookBinding.inflate(layoutInflater)
    }

    private lateinit var passengerList: MutableList<BookingPassenger>
    // private lateinit var seats: List<Seat>
    // private lateinit var seatNames: List<String>
    // private lateinit var seatNamesList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        seatBookView = findViewById(R.id.layout_seat)

        val flightId = intent.getStringExtra("flightId")
        val adultCount = intent.getIntExtra("adultCount", 0)
        val childCount = intent.getIntExtra("childCount", 0)
        val babyCount = intent.getIntExtra("babyCount", 0)
        val price = intent.getIntExtra("price", 0)
        Log.d("SeatBookActivity", "Seat: $flightId")
        val airportCityCodeDeparture = intent.getStringExtra("airportCityCodeDeparture")
        val airportCityCodeDestination = intent.getStringExtra("airportCityCodeDestination")
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        Log.d("SeatBookActivity", "Seat Class: $seatClassChoose")
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()

        passengerList.forEach { passenger ->
            Log.d("SeatBookActivity", "Passenger: $passenger")
        }

        val totalPassengers = adultCount + childCount
        if (flightId != null && seatClassChoose != null) {
            getSeatData(flightId, seatClassChoose)
        }
        // setClickListenerSeat()

        seatBookView.setSelectSeatLimit(totalPassengers)

        binding.tvHeaderDestinationInfo.text = "($airportCityCodeDeparture > $airportCityCodeDestination - $seatClassChoose)"
        binding.layoutSeatbook.typeSeat.text = "$seatClassChoose"

        binding.btnSave.setOnClickListener {
            if (seatBookView.getSelectedSeatCount() == totalPassengers) {
                val intent =
                    Intent(this, FlightDetailPriceActivity::class.java).apply {
                        putExtra("adults", adultCount)
                        putExtra("child", childCount)
                        putExtra("baby", babyCount)
                        putExtra("price", price)
                        putExtra("full_name", intent.getStringExtra("full_name"))
                        putExtra("number_phone", intent.getStringExtra("number_phone"))
                        putExtra("email", intent.getStringExtra("email"))
                        putExtra("family_name", intent.getStringExtra("family_name"))
                        putExtra("flightId", flightId)
                        putExtra("tax", 300000.0)
                        putExtra("promo", 0.0)
                        putExtra("passenger_list", ArrayList(passengerList))
                    }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select seats for all passengers.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getSeatData(
        flightId: String,
        seatClassChoose: String,
    ) {
        seatViewModel.getFormattedSeatData(flightId, seatClassChoose).observe(
            this,
        ) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.csvSeat.setState(ContentState.LOADING)
                    binding.svSeatbook.isVisible = false
                    binding.cvBtnSave.isVisible = false
                },
                doOnSuccess = {
                    binding.csvSeat.setState(ContentState.SUCCESS)
                    binding.svSeatbook.isVisible = true
                    binding.cvBtnSave.isVisible = true
                    val formattedSeatStatus = it.payload?.first.orEmpty()
                    val formattedSeatNames = it.payload?.second.orEmpty()
                    val seatName = it.payload?.third.orEmpty()
                    Log.d("formattedSeatStatus", formattedSeatStatus)
                    Log.d("formattedSeatNames", formattedSeatNames.toString())
                    Log.d("seatName", seatName.toString())
                    showSeatBookView(formattedSeatStatus, formattedSeatNames)
                    setClickListenerSeat(seatName)
                },
                doOnError = {
                    binding.csvSeat.setState(ContentState.ERROR_GENERAL)
                    binding.svSeatbook.isVisible = false
                    binding.cvBtnSave.isVisible = false
                },
                doOnEmpty = {
                    binding.csvSeat.setState(ContentState.EMPTY)
                    binding.svSeatbook.isVisible = false
                    binding.cvBtnSave.isVisible = false
                },
            )
        }
    }

    /*private fun setClickListenerSeat() {
        seatBookView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    if (selectedIdList.isNotEmpty()) {
                        for ((index, selectedSeatId) in selectedIdList.withIndex()) {
                            var seatIndex = selectedSeatId - 1

                            // Validate the seat name
                            var seatName = seatNamesList[seatIndex]
                            while (seatName.isEmpty() || seatName == "/" || seatName.contains("/")) {
                                seatIndex++
                                if (seatIndex >= seatNamesList.size) {
                                    break
                                }
                                seatName = seatNamesList[seatIndex]
                            }

                            if (seatIndex < seatNamesList.size && seatName.isNotEmpty() && seatName != "/" && !seatName.contains("/")) {
                                val seat = findSeatBySeatName(seatName)
                                seat?.let {
                                    val formattedSeatName = it.seatName
                                    Log.d("SeatBookActivity", "Clicked Seat Name: $formattedSeatName")

                                    val seatColumn = formattedSeatName.filter { char -> char.isDigit() }.toInt()
                                    val seatRow = formattedSeatName.filter { char -> char.isLetter() }

                                    if (index < passengerList.size) {
                                        passengerList[index].seatDeparture =
                                            SeatPassenger(
                                                seatRow = seatRow,
                                                seatColumn = seatColumn,
                                            )
                                    }
                                }
                            } else {
                                Log.d("SeatBookActivity", "No valid seat name found after index $selectedSeatId")
                            }
                        }
                    }
                }

                override fun onBookedSeatClick(view: View) {
                }

                override fun onReservedSeatClick(view: View) {
                }
            },
        )

        seatBookView.setSeatLongClickListener(
            object : SeatLongClickListener {
                override fun onAvailableSeatLongClick(view: View) {
                    Toast.makeText(this@SeatBookActivity, "Long Pressed", Toast.LENGTH_SHORT).show()
                }

                override fun onBookedSeatLongClick(view: View) {
                }

                override fun onReservedSeatLongClick(view: View) {
                }
            },
        )
    }*/

    /*private fun findSeatBySeatName(seatName: String): Seat? {
        return seats.find { it.seatName == seatName }
    }*/

    private fun showSeatBookView(
        formattedSeatStatus: String,
        formattedSeatNames: List<String>,
    ) {
        /*this.seats = seats
        val seatClassChoose = intent.getStringExtra("seatClassChoose")
        seatNames =
            when (seatClassChoose) {
                "First Class" -> formatFirstClassSeatNames(seats)
                "Business" -> formatBusinessClassSeatNames(seats)
                "Premium Economy" -> formatPremiumEconomySeatNames(seats)
                else -> formatEconomySeatNames(seats)
            }
        seatNamesList = getSeatNames(seats)
        val seatStatuses = extractSeatStatus(seats, seatClassChoose)
        val seatStatusesFormatted = formatSeatStatus(seatStatuses, seatClassChoose)

        Log.d("SeatBookActivity", "Formatted Seat Names: $seatNames")
        Log.d("SeatBookActivity", "Seat Statuses: $seatStatusesFormatted")*/

        seatBookView.setSeatsLayoutString(formattedSeatStatus)
            .isCustomTitle(true)
            .setCustomTitle(formattedSeatNames)
            .setSeatLayoutPadding(2)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)

        seatBookView.show()
    }

    private fun setClickListenerSeat(seatNamesList: List<String>) {
        seatBookView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    /*if (selectedIdList.isNotEmpty()) {
                        val selectedSeatId = selectedIdList.first()
                        val seatName = seatNamesList[selectedSeatId - 1]
                        val row = seatName.last().toString()
                        val column = seatName.first().digitToIntOrNull() ?: 0
                        Toast.makeText(this@SeatBookActivity, "Seat Row : $row", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@SeatBookActivity, "Seat Column : $column", Toast.LENGTH_SHORT).show()
                        val seatPassenger = SeatPassenger(seatRow = row, seatColumn = column)

                        val passengerIndex = selectedIdList.size - 1
                        if (passengerIndex < passengerList.size) {
                            passengerList[passengerIndex].seatDeparture = seatPassenger
                            // Log the updated passenger info
                            Log.d("SeatBookActivity", "Updated Passenger: ${passengerList[passengerIndex]}")
                        }

                        Toast.makeText(this@SeatBookActivity, "Seat Row : $row, Seat Column : $column", Toast.LENGTH_SHORT).show()
                    }*/
                    if (selectedIdList.isNotEmpty()) {
                        val selectedSeatId = selectedIdList.last()
                        val seatName = seatNamesList[selectedSeatId - 1]
                        val row = seatName.last().toString()
                        val column = seatName.first().toString()
                        Toast.makeText(this@SeatBookActivity, "Seat Row : $row", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@SeatBookActivity, "Seat Column : $column", Toast.LENGTH_SHORT).show()
                        val seatPassenger = SeatPassenger(seatRow = row, seatColumn = column)

                        val nextPassengerIndex = passengerList.indexOfFirst { it.seatDeparture == null }
                        if (nextPassengerIndex != -1) {
                            passengerList[nextPassengerIndex].seatDeparture = seatPassenger
                            Log.d("SeatBookActivity", "Updated Passenger: ${passengerList[nextPassengerIndex]}")
                        }

                        Toast.makeText(this@SeatBookActivity, "Seat Row : $row, Seat Column : $column", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBookedSeatClick(view: View) {
                }

                override fun onReservedSeatClick(view: View) {
                }
            },
        )
    }

    private fun formatFirstClassSeatNames(seats: List<Seat>): List<String> {
        val seatNames = mutableListOf<String>()
        val maxRow = 2
        val maxCol = 2

        for (col in 1..maxCol) {
            seatNames.add("/")

            for (row in 1..maxRow) {
                val rowChar = 'A' + (row - 1)
                val seatName =
                    seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatName
                        ?: "$col$rowChar"
                seatNames.add(seatName)
                if (row < maxCol) {
                    seatNames.add("")
                }
            }
        }

        return seatNames
    }

    private fun formatBusinessClassSeatNames(seats: List<Seat>): List<String> {
        val seatNames = mutableListOf<String>()
        val maxRow = 4
        val maxCol = 5

        for (col in 1..maxCol) {
            if (col > 1) {
                seatNames.add("/")
            }

            for (row in 1..maxRow) {
                val rowChar = 'A' + (row - 1)
                val seatName =
                    seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatName
                        ?: "${rowChar}$col"
                seatNames.add(seatName)

                if (row == 2) {
                    seatNames.add("")
                }
            }
        }

        return seatNames
    }

    private fun formatPremiumEconomySeatNames(seats: List<Seat>): List<String> {
        val seatNames = mutableListOf<String>()
        val maxRow = 4
        val maxCol = 6

        for (col in 1..maxCol) {
            if (col > 1) {
                seatNames.add("/")
            }

            for (row in 1..maxRow) {
                val rowChar = 'A' + (row - 1)
                val seatName =
                    seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatName
                        ?: "${rowChar}$col"
                seatNames.add(seatName)

                if (row == 2) {
                    seatNames.add("")
                }
            }
        }

        return seatNames
    }

    private fun formatEconomySeatNames(seats: List<Seat>): List<String> {
        val seatNames = mutableListOf<String>()

        // Find the maximum row and column numbers
        val maxRow = seats.maxOf { it.seatRow.first() }.toInt() - 'A'.toInt() + 1
        val maxCol = seats.maxOf { it.seatColumn }

        for (col in 1..maxCol) {
            seatNames.add("/")

            var isEmptyAdded = false

            for (row in 0 until maxRow) {
                val rowChar = 'A' + row

                val seatName =
                    seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatName
                        ?: "$col$rowChar"

                if (row % 3 == 0 && row != 0) {
                    seatNames.add("")
                    isEmptyAdded = true

                    val rowChar1D = 'A' + 3
                    val seatName1D =
                        seats.find { it.seatRow == rowChar1D.toString() && it.seatColumn == col }?.seatName
                            ?: "$col$rowChar1D"
                    seatNames.add(seatName1D)
                } else {
                    seatNames.add(seatName)
                }
            }
        }

        return seatNames
    }

    /*private fun extractSeatStatus(seats: List<Seat>): String {
        val seatStatusBuilder = StringBuilder()
        seats.forEach { seat ->
            seatStatusBuilder.append(seat.seatStatusAndroid)
        }
        return seatStatusBuilder.toString()
    }*/

    /*private fun formatSeatStatus(seatStatus: String): String {
        val formattedBuilder = StringBuilder()
        formattedBuilder.append("/")
        var isSlash = true // flag to alternate between "/" and "_"
        for (i in seatStatus.indices) {
            if (i % 3 == 0 && i != 0) {
                if (isSlash) {
                    formattedBuilder.append("_")
                } else {
                    formattedBuilder.append("/")
                }
                isSlash = !isSlash // toggle the flag
            }
            formattedBuilder.append(seatStatus[i])
        }
        return formattedBuilder.toString()
    }*/

    private fun extractSeatStatus(
        seats: List<Seat>,
        seatClassChoose: String?,
    ): String {
        val seatStatusBuilder = StringBuilder()

        when (seatClassChoose) {
            "First Class" -> {
                val maxRow = 2
                val maxCol = 2
                for (col in 1..maxCol) {
                    seatStatusBuilder.append("/")
                    for (row in 1..maxRow) {
                        val rowChar = 'A' + (row - 1)
                        val seatStatus =
                            seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatStatusAndroid
                                ?: "A"
                        seatStatusBuilder.append(seatStatus)
                    }
                }
            }
            "Business" -> {
                val maxRow = 4
                val maxCol = 5
                for (col in 1..maxCol) {
                    seatStatusBuilder.append("/")
                    for (row in 1..maxRow) {
                        val rowChar = 'A' + (row - 1)
                        val seatStatus =
                            seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatStatusAndroid
                                ?: "A"
                        seatStatusBuilder.append(seatStatus)
                    }
                }
            }
            "Premium Economy" -> {
                val maxRow = 6
                val maxCol = 4
                for (col in 1..maxCol) {
                    seatStatusBuilder.append("/")
                    for (row in 1..maxRow) {
                        val rowChar = 'A' + (row - 1)
                        val seatStatus =
                            seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatStatusAndroid
                                ?: "A"
                        seatStatusBuilder.append(seatStatus)
                    }
                }
            }
            else -> {
                seats.forEach { seat ->
                    seatStatusBuilder.append(seat.seatStatusAndroid)
                }
            }
        }

        return seatStatusBuilder.toString()
    }

    private fun formatSeatStatus(
        seatStatus: String,
        seatClassChoose: String?,
    ): String {
        val formattedBuilder = StringBuilder()
        var isSlash = true

        when (seatClassChoose) {
            "First Class" -> {
                var isFirstChar = true

                for (i in seatStatus.indices) {
                    if (seatStatus[i] == '/') {
                        formattedBuilder.append("/")
                        isFirstChar = true
                    } else {
                        if (!isFirstChar && i % 2 == 0) {
                            formattedBuilder.append("_")
                        }
                        formattedBuilder.append(seatStatus[i])
                        isFirstChar = false
                    }
                }
            }
            "Business Class" -> {
                for (i in seatStatus.indices) {
                    if (i % 5 == 0 && i != 0) {
                        formattedBuilder.append("/")
                    }
                    formattedBuilder.append(seatStatus[i])
                }
            }
            "Premium Economy" -> {
                for (i in seatStatus.indices) {
                    if (i % 6 == 0 && i != 0) {
                        formattedBuilder.append("/")
                    }
                    formattedBuilder.append(seatStatus[i])
                }
            }
            else -> {
                for (i in seatStatus.indices) {
                    if (i % 3 == 0 && i != 0) {
                        if (isSlash) {
                            formattedBuilder.append("_")
                        } else {
                            formattedBuilder.append("/")
                        }
                        isSlash = !isSlash // Toggle flag isSlash
                    }
                    formattedBuilder.append(seatStatus[i])
                }
            }
        }

        return formattedBuilder.toString()
    }

    private fun getSeatNames(seats: List<Seat>): List<String> {
        return seats.sortedWith(compareBy({ it.seatColumn }, { it.seatRow }))
            .map { it.seatName }
    }
}
