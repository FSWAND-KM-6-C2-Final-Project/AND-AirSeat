package com.c2.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DepartureAirport(
    @SerializedName("airport_city")
    val airportCity: String?,
    @SerializedName("airport_city_code")
    val airportCityCode: String?,
    @SerializedName("airport_continent")
    val airportContinent: String?,
    @SerializedName("airport_name")
    val airportName: String?,
    @SerializedName("airport_picture")
    val airportPicture: String?,
)
