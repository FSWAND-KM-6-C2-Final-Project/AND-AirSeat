package com.nafi.airseat.utils.seatbook

import android.view.View

interface SeatLongClickListener {
    fun onAvailableSeatLongClick(view: View)

    fun onBookedSeatLongClick(view: View)

    fun onReservedSeatLongClick(view: View)
}
