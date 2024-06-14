package com.nafi.airseat.data.source.network.model.flightdetail

import com.google.gson.annotations.SerializedName

data class FlightDetailDepartureResponse(
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
