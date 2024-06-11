package com.nafi.airseat.data.source.network.model.seat

import com.google.gson.annotations.SerializedName

data class Seat(
    @SerializedName("id")
    val id: Int,
    @SerializedName("seat_row")
    val seatRow: String,
    @SerializedName("seat_column")
    val seatColumn: Int,
    @SerializedName("seat_name")
    val seatName: String,
    @SerializedName("flight_id")
    val flightId: Int,
    @SerializedName("class")
    val classAir: String,
    @SerializedName("seat_status")
    val seatStatus: String,
    @SerializedName("seat_status_android")
    val seatStatusAndroid: String,
)
