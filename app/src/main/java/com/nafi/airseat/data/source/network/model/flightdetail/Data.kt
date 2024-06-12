package com.nafi.airseat.data.source.network.model.flightdetail

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Data(
    @SerializedName("flight")
    val flight: FlightDataDetail?,
)
