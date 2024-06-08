package com.nafi.airseat.data.source.network.model.airline

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirlinesResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("data")
    val data: AirlineData?,
)
