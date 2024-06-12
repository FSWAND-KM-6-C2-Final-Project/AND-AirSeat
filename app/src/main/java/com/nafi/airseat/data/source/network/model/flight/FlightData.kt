package com.nafi.airseat.data.source.network.model.flight

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FlightData(
    @SerializedName("flights")
    val flight: List<FlightItemResponse>?,
)
