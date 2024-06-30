package com.c2.airseat.data.source.network.model.flightdetail

import com.google.gson.annotations.SerializedName

data class FlightDetailAirlineResponse(
    @SerializedName("airline_name")
    val airlineName: String?,
    @SerializedName("airline_picture")
    val airlinePicture: String?,
)
