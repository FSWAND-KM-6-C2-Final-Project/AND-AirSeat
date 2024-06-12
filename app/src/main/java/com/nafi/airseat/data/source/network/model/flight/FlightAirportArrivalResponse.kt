package com.nafi.airseat.data.source.network.model.flight

import com.google.gson.annotations.SerializedName

data class FlightAirportArrivalResponse(
    @SerializedName("airport_name")
    val airportName: String?,
    @SerializedName("airport_city")
    val airportCity: String?,
    @SerializedName("airport_city_code")
    val airportCityCode: String?,
    @SerializedName("airport_picture")
    val airportPicture: String?,
    @SerializedName("airport_continent")
    val airportContinent: String?,
)
