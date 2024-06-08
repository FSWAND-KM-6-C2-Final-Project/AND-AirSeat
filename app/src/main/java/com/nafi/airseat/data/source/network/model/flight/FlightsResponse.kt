package com.nafi.airseat.data.source.network.model.flight

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FlightsResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("data")
    val data: FlightData?,
)
