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
    val airline: FlightDetailAirline,
    val departureAirport: FlightDetailDeparture,
    val arrivalAirport: FlightDetailArrival,
    val duration: String,
)

data class FlightDetailAirline(
    val airlineName: String,
    val airlinePicture: String,
)

data class FlightDetailDeparture(
    val airportName: String,
    val airportCity: String,
    val airportCityCode: String,
    val airportPicture: String,
    val airportContinent: String,
)

data class FlightDetailArrival(
    val airportName: String,
    val airportCity: String,
    val airportCityCode: String,
    val airportPicture: String,
    val airportContinent: String,
)
