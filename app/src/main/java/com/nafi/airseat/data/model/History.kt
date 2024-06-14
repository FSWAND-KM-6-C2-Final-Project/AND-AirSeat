package com.nafi.airseat.data.model

data class History(
    val bookingCode: String,
    val bookingStatus: String,
    val classes: String,
    val createdAt: String,
    val duration: String,
    val flight: HistoryFlight,
    val returnFlight: HistoryReturnFlight?,
    val totalAmount: Long,
    val bookingDetail: List<BookingDetailHistory>,
)

data class HistoryFlight(
    val arrivalAirport: HistoryArrivalAirport,
    val arrivalTime: String,
    val departureAirport: HistoryDepartureAirport,
    val departureTerminal: String,
    val departureTime: String,
    val flightNumber: String,
    val information: String,
)

data class HistoryReturnFlight(
    val arrivalAirport: HistoryReturnArrivalAirport,
    val arrivalTime: String,
    val departureAirport: HistoryReturnDepartureAirport,
    val departureTerminal: String,
    val departureTime: String,
    val flightNumber: String,
    val information: String,
)

data class HistoryArrivalAirport(
    val airportCity: String,
    val airportCityCode: String,
    val airportContinent: String,
    val airportName: String,
    val airportPicture: String,
)

data class HistoryDepartureAirport(
    val airportCity: String,
    val airportCityCode: String,
    val airportContinent: String,
    val airportName: String,
    val airportPicture: String,
)

data class HistoryReturnArrivalAirport(
    val airportCity: String,
    val airportCityCode: String,
    val airportContinent: String,
    val airportName: String,
    val airportPicture: String,
)

data class HistoryReturnDepartureAirport(
    val airportCity: String,
    val airportCityCode: String,
    val airportContinent: String,
    val airportName: String,
    val airportPicture: String,
)

data class BookingDetailHistory(
    val price: Int,
    val seat: SeatHistory,
    val passenger: PassengerHistory,
)

data class SeatHistory(
    val classes: String,
    val seatName: String,
)

data class PassengerHistory(
    val createdAt: String?,
    val dob: String?,
    val firstName: String?,
    val id: Int?,
    val identificationCountry: String?,
    val identificationExpired: String?,
    val identificationNumber: String?,
    val identificationType: String?,
    val lastName: String?,
    val nationality: String?,
    val title: String?,
    val updatedAt: String?,
)
