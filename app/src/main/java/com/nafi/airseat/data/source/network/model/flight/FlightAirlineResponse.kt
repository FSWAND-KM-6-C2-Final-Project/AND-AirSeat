package com.nafi.airseat.data.source.network.model.flight

import com.google.gson.annotations.SerializedName

data class FlightAirlineResponse(
    @SerializedName("airline_name")
    val airlineName: String?,
    @SerializedName("airline_picture")
    val airlinePicture: String?,
)
