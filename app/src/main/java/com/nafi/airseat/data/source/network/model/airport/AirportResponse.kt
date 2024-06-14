package com.nafi.airseat.data.source.network.model.airport

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirportResponse(
    @SerializedName("data")
    val data: AirportData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("status")
    val status: String?,
)
