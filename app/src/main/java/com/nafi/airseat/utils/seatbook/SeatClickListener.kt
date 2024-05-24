package com.nafi.airseat.utils.seatbook

import android.view.View

interface SeatClickListener {
    fun onAvailableSeatClick(
        selectedIdList: List<Int>,
        view: View,
    )

    fun onBookedSeatClick(view: View)

    fun onReservedSeatClick(view: View)
}
