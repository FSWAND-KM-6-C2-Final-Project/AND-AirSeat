package com.nafi.airseat.presentation.seatbook

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.R
import com.nafi.airseat.utils.seatbook.SeatBookView
import dev.jahidhasanco.seatbookview.SeatClickListener
import dev.jahidhasanco.seatbookview.SeatLongClickListener

class SeatBookActivity : AppCompatActivity() {
    private lateinit var seatBookView: SeatBookView

    private val seatViewModel: SeatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_book)
        seatBookView = findViewById(R.id.layout_seat)

        /*seatBookView.setSeatsLayoutString(seatsLayout)
            .isCustomTitle(true)
            .setCustomTitle(title)
            .setSeatLayoutPadding(2)
            .setSeatSizeBySeatsColumnAndLayoutWidth(7, -1)*/

        seatBookView.show()

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
}
