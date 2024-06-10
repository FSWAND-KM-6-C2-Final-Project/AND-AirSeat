package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seat(
    var seatRow: String,
    var seatColumn: Int,
    var seatName: String,
    var flightId: Int? = null,
    var seatStatusAndroid: String,
) : Parcelable
