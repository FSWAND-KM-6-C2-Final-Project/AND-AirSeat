package com.nafi.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BookingDetail(
    @SerializedName("price")
    val price: String?,
    @SerializedName("seat")
    val seat: Seat?,
)
