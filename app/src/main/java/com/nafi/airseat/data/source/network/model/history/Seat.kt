package com.nafi.airseat.data.source.network.model.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Seat(
    @SerializedName("class")
    val classX: String?,
    @SerializedName("seat_name")
    val seatName: String?
)