package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeatClass(
    val id: Int?,
    val seatName: String,
    val seatPrice: Int,
    var isSelected: Boolean = false,
) : Parcelable
