package com.nafi.airseat.data.model

data class FlightDetailPrice(
    val destination: String,
    val flightTime: String,
    val adults: Int,
    val adultsPrice: Double,
    val baby: Int,
    val babyPrice: Double,
    val child: Int,
    val childPrice: Double,
    val tax: Double,
    val promo: Double,
    val totalPrice: Double,
)

data class FlightDetail(
    val departureTime: String,
    val departureDate: String,
    val departureAirport: String,
    val airplane: String,
    val codeAirplane: String,
    val baggage: String,
    val cabinBaggage: String,
    val flightEntertainment: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val arrivalAirport: String,
    val flightDetailPrice: FlightDetailPrice,
)
