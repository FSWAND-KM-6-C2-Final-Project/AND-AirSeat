package com.c2.airseat.data.source.network.model.airport

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirportData(
    @SerializedName("airports")
    val airports: List<AirportDetail>?,
    @SerializedName("pagination")
    val pagination: Pagination?,
)
