package com.c2.airseat.data.source.network.model.flightdetail

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FlightDetailResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("status")
    val status: String?,
)
