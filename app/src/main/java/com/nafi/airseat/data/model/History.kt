package com.nafi.airseat.data.model

data class History(
    val bookingCode: String?,
    val bookingStatus: String?,
    val classes: String?,
    val createdAt: String?,
    val duration: String?,
    val flight: HistoryFlight?,
    val returnFlight: HistoryFlight?,
    val totalAmount: String?
)

data class HistoryFlight(
    val arrivalAirport: HistoryArrivalAirport?,
    val arrivalTime: String?,
    val departureAirport: HistoryDepartureAirport?,
    val departureTerminal: String?,
    val departureTime: String?,
    val flightNumber: String?,
    val information: String?
)

data class HistoryArrivalAirport(
    val airportCity: String?,
    val airportCityCode: String?,
    val airportContinent: String?,
    val airportName: String?,
    val airportPicture: String?
)

data class HistoryDepartureAirport(
    val airportCity: String?,
    val airportCityCode: String?,
    val airportContinent: String?,
    val airportName: String?,
    val airportPicture: String?
)