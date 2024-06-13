package com.nafi.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ReturnFlight(
    @SerializedName("airline")
    val airline: Airline?,
    @SerializedName("arrivalAirport")
    val arrivalAirport: ReturnArrivalAirport?,
    @SerializedName("arrival_time")
    val arrivalTime: String?,
    @SerializedName("departureAirport")
    val departureAirport: ReturnDepartureAirport?,
    @SerializedName("departure_terminal")
    val departureTerminal: String?,
    @SerializedName("departure_time")
    val departureTime: String?,
    @SerializedName("flight_number")
    val flightNumber: String?,
    @SerializedName("information")
    val information: String?,
)
