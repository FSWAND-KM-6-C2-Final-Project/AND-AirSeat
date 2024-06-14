package com.nafi.airseat.data.source.network.model.flightdetail

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FlightDataDetail(
    @SerializedName("airline_id")
    val airlineId: Int?,
    @SerializedName("arrival_airport_id")
    val arrivalAirportId: Int?,
    @SerializedName("arrival_time")
    val arrivalTime: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("departure_airport_id")
    val departureAirportId: Int?,
    @SerializedName("departure_terminal")
    val departureTerminal: String?,
    @SerializedName("departure_time")
    val departureTime: String?,
    @SerializedName("flight_number")
    val flightNumber: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("information")
    val information: String?,
    @SerializedName("price_business")
    val priceBusiness: String?,
    @SerializedName("price_economy")
    val priceEconomy: String?,
    @SerializedName("price_first_class")
    val priceFirstClass: String?,
    @SerializedName("price_premium_economy")
    val pricePremiumEconomy: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("airline")
    val airline: FlightDetailAirlineResponse,
    @SerializedName("departureAirport")
    val departureAirport: FlightDetailDepartureResponse,
    @SerializedName("arrivalAirport")
    val arrivalAirport: FlightDetailArrivalResponse,
)
