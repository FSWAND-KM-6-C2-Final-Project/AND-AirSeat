package com.nafi.airseat.data.source.network.model.booking

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingPassenger(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val familyName: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("identification_type")
    val identificationType: String?,
    @SerializedName("identification_number")
    val identificationNumber: String?,
    @SerializedName("identification_country")
    val identificationCountry: String?,
    @SerializedName("identification_expired")
    val identificationExpired: String?,
    @SerializedName("passenger_type")
    val passengerType: String,
    @SerializedName("seat_departure")
    var seatDeparture: SeatPassenger? = null,
    @SerializedName("seat_return")
    var seatReturn: SeatPassenger? = null,
) : Parcelable

@Parcelize
data class SeatPassenger(
    @SerializedName("seat_row")
    var seatRow: String,
    @SerializedName("seat_column")
    var seatColumn: String,
) : Parcelable
