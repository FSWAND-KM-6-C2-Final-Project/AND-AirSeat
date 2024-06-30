package com.c2.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class HistoryData(
    @SerializedName("booking")
    val booking: List<Booking>?,
)
