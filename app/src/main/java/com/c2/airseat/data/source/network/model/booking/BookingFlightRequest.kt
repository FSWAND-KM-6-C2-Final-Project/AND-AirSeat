package com.c2.airseat.data.source.network.model.booking

import com.google.gson.annotations.SerializedName

data class BookingFlightRequest(
    @SerializedName("flight_id")
    val flightId: Int,
    @SerializedName("return_flight_id")
    val returnFlightId: Int?,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("ordered_by")
    val orderedBy: OrderedBy,
    @SerializedName("passenger")
    val passenger: List<BookingPassenger>,
)
