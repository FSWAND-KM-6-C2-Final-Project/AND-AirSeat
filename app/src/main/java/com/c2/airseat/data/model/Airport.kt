package com.c2.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Airport(
    var id: Int,
    var airportName: String,
    var airportCity: String,
    var airportCityCode: String,
    var airportPicture: String,
) : Parcelable
