package com.nafi.airseat.data.network.seat

import com.google.gson.annotations.SerializedName

data class SeatData(
    @SerializedName("seats")
    val seats: List<Seat>,
)
