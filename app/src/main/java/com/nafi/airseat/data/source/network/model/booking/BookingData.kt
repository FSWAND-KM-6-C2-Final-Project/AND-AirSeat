package com.nafi.airseat.data.source.network.model.booking

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BookingData(
    @SerializedName("booking")
    val booking: Booking,
    @SerializedName("payment_data")
    val paymentData: PaymentData,
)
