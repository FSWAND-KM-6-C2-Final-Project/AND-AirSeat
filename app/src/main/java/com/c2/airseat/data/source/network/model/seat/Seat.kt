package com.c2.airseat.data.source.network.model.seat

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable
