package com.nafi.airseat.data.source.network.model.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Data(
    @SerializedName("booking")
    val booking: List<Booking?>?
)