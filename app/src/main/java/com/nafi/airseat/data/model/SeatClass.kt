package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeatClass(
    val id: Int, // or any other unique identifier
    val seatName: String,
    val seatPrice: Int,
) : Parcelable
