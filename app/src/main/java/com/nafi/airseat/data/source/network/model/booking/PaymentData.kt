package com.nafi.airseat.data.source.network.model.booking

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PaymentData(
    @SerializedName("token")
    val token: String,
    @SerializedName("redirect_url")
    val redirectUrl: String,
)
