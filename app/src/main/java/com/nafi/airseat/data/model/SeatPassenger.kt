package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeatPassenger(
    var seatRow: String,
    var seatColumn: Int,
) : Parcelable
