package com.nafi.airseat.presentation.seatbook

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.R
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.utils.proceedWhen
import com.nafi.airseat.utils.seatbook.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import dev.jahidhasanco.seatbookview.SeatLongClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeatBookActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView

    private val seatViewModel: SeatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_book)
        seatBookView = findViewById(R.id.layout_seat)

        getSeatData()
        setClickListenerSeat()
    }

    private fun getSeatData() {
        seatViewModel.getSeatData().observe(
            this,
        ) { result ->
            result.proceedWhen(
                doOnLoading = {
                },
                doOnSuccess = {
                    result.payload?.let {
                        showSeatBookView(it)
                    }
                },
                doOnError = {
                },
                doOnEmpty = {
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

    private fun showSeatBookView(seats: List<Seat>) {
        // Format seat names and statuses
        val seatNames = formatSeatNames(seats)
        val seatStatuses = extractSeatStatus(seats)
        val seatStatusesFormatted = formatSeatStatus(seatStatuses)

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
        val maxRow = seats.maxOf { it.seatColumn }
        val maxCol = 12 // Adjust this value if your seating configuration has more columns

        for (row in 1..maxRow) {
            seatNames.add("/") // Add the slash at the beginning of each row
            for (col in listOf("A", "B", "C", "", "D", "E", "F")) {
                if (col.isEmpty()) {
                    seatNames.add("") // Add empty string for the gap
                } else {
                    val seatName = seats.find { it.seatRow == col && it.seatColumn == row }?.seatName ?: ""
                    seatNames.add("$col$row")
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
