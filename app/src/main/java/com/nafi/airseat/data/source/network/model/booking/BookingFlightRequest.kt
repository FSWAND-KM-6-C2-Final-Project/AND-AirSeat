package com.nafi.airseat.data.source.network.model.booking

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.model.Passenger

data class BookingFlightRequest(
    @SerializedName("flight_id")
    val flightId: Int,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("discount_id")
    val discountId: Int,
    @SerializedName("ordered_by")
    val orderedBy: OrderedBy,
    @SerializedName("passenger")
    val passenger: List<Passenger>,
)
