package com.c2.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Seat(
    @SerializedName("class")
    val classX: String?,
    @SerializedName("seat_name")
    val seatName: String?,
)
