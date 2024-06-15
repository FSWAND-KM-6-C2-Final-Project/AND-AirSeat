package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Passenger(
    val firstName: String,
    val familyName: String,
    val title: String,
    val dob: String,
    val nationality: String,
    val identificationType: String,
    val identificationNumber: String,
    val identificationCountry: String,
    val identificationExpired: String,
    var seatDeparture: SeatPassenger? = null,
) : Parcelable
