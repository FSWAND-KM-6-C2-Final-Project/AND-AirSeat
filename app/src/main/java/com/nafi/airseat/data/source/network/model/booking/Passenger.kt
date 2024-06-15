package com.nafi.airseat.data.source.network.model.booking

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.source.network.model.seat.Seat

data class Passenger(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("identification_type")
    val identificationType: String,
    @SerializedName("identification_number")
    val identificationNumber: String,
    @SerializedName("identification_country")
    val identificationCountry: String,
    @SerializedName("identification_expired")
    val identificationExpired: String,
    @SerializedName("seat_departure")
    val seatDeparture: Seat,
)
