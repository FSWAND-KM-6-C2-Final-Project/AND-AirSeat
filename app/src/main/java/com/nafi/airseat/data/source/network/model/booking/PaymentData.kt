package com.nafi.airseat.data.source.network.model.booking

import com.google.gson.annotations.SerializedName

data class PaymentData(
    @SerializedName("token")
    val token: String,
    @SerializedName("redirect_url")
    val redirectUrl: String,
)
