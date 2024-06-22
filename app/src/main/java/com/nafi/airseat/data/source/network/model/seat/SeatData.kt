package com.nafi.airseat.data.source.network.model.seat

import com.google.gson.annotations.SerializedName

data class SeatData(
    @SerializedName("seats")
    val seats: List<Seat>,
)
