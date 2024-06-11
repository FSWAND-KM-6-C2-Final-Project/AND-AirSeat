package com.nafi.airseat.data.source.network.model.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BookingDetail(
    @SerializedName("price")
    val price: String?,
    @SerializedName("seat")
    val seat: Seat?
)