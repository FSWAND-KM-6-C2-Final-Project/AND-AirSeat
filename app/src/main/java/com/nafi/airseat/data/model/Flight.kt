package com.nafi.airseat.data.model

data class Flight(
    var id: Int,
    var flightNumber: String,
    var information: String,
    var departureTime: String,
    var arrivalTime: String,
    var departureAirportId: Int,
    var departureTerminal: String,
    var arrivalAirportId: Int,
    var priceEconomy: Int,
    var pricePremiumEconomy: Int,
    var priceBusiness: Int,
    var priceFirstClass: Int,
    var airlineId: Int,
    var createdAt: String,
    var updatedAt: String,
)
