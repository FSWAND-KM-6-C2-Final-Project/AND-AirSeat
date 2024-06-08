package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Airport(
    var id: Int?,
    var airportName: String,
    var airportCity: String,
    var airportCityCode: String,
    var airportPicture: String,
    var airportContinent: String,
    var createdAt: String,
    var updatedAt: String,
) : Parcelable
