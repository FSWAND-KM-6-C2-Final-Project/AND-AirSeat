package com.nafi.airseat.data.source.network.model.airline

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirlineData(
    @SerializedName("airline")
    val airline: List<AirlineItemResponse>?,
)
