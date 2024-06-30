package com.c2.airseat.data.source.network.model.seat

import com.google.gson.annotations.SerializedName

data class SeatResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("requestAt")
    val requestAt: String,
    @SerializedName("data")
    val data: SeatData,
)
