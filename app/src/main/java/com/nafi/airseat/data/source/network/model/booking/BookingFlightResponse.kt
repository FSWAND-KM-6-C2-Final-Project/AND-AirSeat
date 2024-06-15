package com.nafi.airseat.data.source.network.model.booking

import com.google.gson.annotations.SerializedName

data class BookingFlightResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var booking: BookingFlightRequest,
)
