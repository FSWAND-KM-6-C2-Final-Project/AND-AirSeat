package com.nafi.airseat.data.source.network.model.flight

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FlightItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("flight_number")
    val flightNumber: String?,
    @SerializedName("information")
    val information: String?,
    @SerializedName("departure_time")
    val departureTime: String?,
    @SerializedName("arrival_time")
    val arrivalTime: String?,
    @SerializedName("departure_airport_id")
    val departureAirportId: Int?,
    @SerializedName("departure_terminal")
    val departureTerminal: String?,
    @SerializedName("arrival_airport_id")
    val arrivalAirportId: Int?,
    @SerializedName("price_economy")
    val priceEconomy: Int?,
    @SerializedName("price_premium_economy")
    val pricePremiumEconomy: Int?,
    @SerializedName("price_business")
    val priceBusiness: Int?,
    @SerializedName("price_first_class")
    val priceFirstClass: Int?,
    @SerializedName("airline_id")
    val airlineId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("airline")
    val airline: FlightAirlineResponse,
    @SerializedName("departureAirport")
    val departureAirport: FlightAirportDepartureResponse,
    @SerializedName("arrivalAirport")
    val arrivalAirport: FlightAirportArrivalResponse,
    @SerializedName("duration")
    val duration: String?,
)
