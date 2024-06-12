package com.nafi.airseat.data.model

data class FlightDetail(
    val airlineId: Int,
    val arrivalAirportId: Int,
    val arrivalTime: String,
    val createdAt: String,
    val departureAirportId: Int,
    val departureTerminal: String,
    val departureTime: String,
    val flightNumber: String,
    val id: Int,
    val information: String,
    val priceBusiness: String,
    val priceEconomy: String,
    val priceFirstClass: String,
    val pricePremiumEconomy: String,
    val updatedAt: String,
)
