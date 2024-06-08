package com.nafi.airseat.data.source.network.model.airport

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirportData(
    @SerializedName("airport")
    val airport: List<AirportItemResponse>?,
)
