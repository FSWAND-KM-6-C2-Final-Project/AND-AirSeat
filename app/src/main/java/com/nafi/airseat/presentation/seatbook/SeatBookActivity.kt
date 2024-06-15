package com.nafi.airseat.presentation.seatbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nafi.airseat.R
import com.nafi.airseat.data.model.Passenger
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.databinding.ActivitySeatBookBinding
import com.nafi.airseat.presentation.common.views.ContentState
import com.nafi.airseat.presentation.flightdetail.FlightDetailActivity
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.seatbook.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import dev.jahidhasanco.seatbookview.SeatLongClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeatBookActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView

    private val seatViewModel: SeatViewModel by viewModel()

    private val binding: ActivitySeatBookBinding by lazy {
        ActivitySeatBookBinding.inflate(layoutInflater)
    }

    private lateinit var passengerList: MutableList<Passenger>
    private lateinit var seats: List<Seat>
    private lateinit var seatNames: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        seatBookView = findViewById(R.id.layout_seat)

        val adultCount = intent.getIntExtra("adult_count", 0)
        val childCount = intent.getIntExtra("child_count", 0)
        val babyCount = intent.getIntExtra("baby_count", 0)
        passengerList = intent.getParcelableArrayListExtra("passenger_list") ?: mutableListOf()

        passengerList.forEach { passenger ->
            Log.d("SeatBookActivity", "Passenger: $passenger")
        }

        val totalPassengers = adultCount + childCount + babyCount
        getSeatData()
        setClickListenerSeat()

        seatBookView.setSelectSeatLimit(totalPassengers)

        binding.btnSave.setOnClickListener {
            if (seatBookView.getSelectedSeatCount() == totalPassengers) {
                val intent =
                    Intent(this, FlightDetailActivity::class.java).apply {
                        putExtra("adults", adultCount)
                        putExtra("child", childCount)
                        putExtra("baby", babyCount)
                        putExtra("full_name", intent.getStringExtra("full_name"))
                        putExtra("number_phone", intent.getStringExtra("number_phone"))
                        putExtra("email", intent.getStringExtra("email"))
                        putExtra("family_name", intent.getStringExtra("family_name"))
                        putExtra("adultsPrice", 3550000.0)
                        putExtra("childPrice", 950000.0)
                        putExtra("babyPrice", 350000.0)
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

    private fun getSeatData() {
        seatViewModel.getSeatData().observe(
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
                    result.payload?.let {
                        showSeatBookView(it)
                    }
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

    private fun setClickListenerSeat() {
        seatBookView.setSeatClickListener(
            object : SeatClickListener {
                override fun onAvailableSeatClick(
                    selectedIdList: List<Int>,
                    view: View,
                ) {
                    if (selectedIdList.isNotEmpty()) {
                        val selectedSeatId = selectedIdList.first()
                        val seatName = seatNames[selectedSeatId - 1] // Adjust index if needed

                        val seat = findSeatBySeatName(seatName)
                        seat?.let {
                            val formattedSeatName = it.seatName
                            Log.d("SeatBookActivity", "Clicked Seat Name: $formattedSeatName")
                            // Use formattedSeatName as needed
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
    }

    private fun findSeatBySeatName(seatName: String): Seat? {
        return seats.find { it.seatName == seatName }
    }

    private fun showSeatBookView(seats: List<Seat>) {
        this.seats = seats
        // Format seat names and statuses
        seatNames = formatSeatNames(seats)
        val seatStatuses = extractSeatStatus(seats)
        val seatStatusesFormatted = formatSeatStatus(seatStatuses)

        Log.d("SeatBookActivity", "Formatted Seat Names: $seatNames")
        Log.d("SeatBookActivity", "Seat Statuses: $seatStatusesFormatted")

        // Assuming seatBookView is already initialized somewhere
        seatBookView.setSeatsLayoutString(seatStatusesFormatted)
            .isCustomTitle(true)
            .setCustomTitle(seatNames)
            .setSeatLayoutPadding(2)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)

        seatBookView.show()
    }

    private fun formatSeatNames(seats: List<Seat>): List<String> {
        val seatNames = mutableListOf<String>()

        // Find the maximum row and column numbers
        val maxRow = seats.maxOf { it.seatRow.first() }.toInt() - 'A'.toInt() + 1
        val maxCol = seats.maxOf { it.seatColumn } // Adjust this value if your seating configuration has more columns

        for (col in 1..maxCol) {
            seatNames.add("/") // Add the slash at the beginning of each row

            // Track if an empty string is added
            var isEmptyAdded = false

            for (row in 0 until maxRow) {
                val rowChar = ('A' + row).toChar()

                // Find seat by row and column
                val seatName =
                    seats.find { it.seatRow == rowChar.toString() && it.seatColumn == col }?.seatName
                        ?: "$col$rowChar"

                // Check if it's time to add an empty string
                if (row % 3 == 0 && row != 0) {
                    seatNames.add("") // Add empty string for the gap after every 3rd row
                    isEmptyAdded = true

                    // After adding the empty string, add "1D"
                    val rowChar1D = ('A' + 3).toChar() // Adding "1D" after the gap
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

    private fun extractSeatStatus(seats: List<Seat>): String {
        val seatStatusBuilder = StringBuilder()
        seats.forEach { seat ->
            seatStatusBuilder.append(seat.seatStatusAndroid)
        }
        return seatStatusBuilder.toString()
    }

    private fun formatSeatStatus(seatStatus: String): String {
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
    }
}
