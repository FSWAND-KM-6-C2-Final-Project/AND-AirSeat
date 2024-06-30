package com.c2.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BookingDetail(
    @SerializedName("price")
    val price: Int?,
    @SerializedName("seat")
    val seat: Seat,
    @SerializedName("passenger")
    val passenger: Passenger,
)
