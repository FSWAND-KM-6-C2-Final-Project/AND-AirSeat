package com.c2.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
    var airline: FlightAirline,
    var departureAirport: FlightAirportDeparture,
    var arrivalAirport: FlightAirportArrival,
    var duration: String,
) : Parcelable

@Parcelize
data class FlightAirline(
    var airlineName: String,
    var airlinePicture: String,
) : Parcelable

@Parcelize
data class FlightAirportDeparture(
    var airportName: String,
    var airportCity: String,
    var airportCityCode: String,
    var airportPicture: String,
    val airportContinent: String,
) : Parcelable

@Parcelize
data class FlightAirportArrival(
    var airportName: String,
    var airportCity: String,
    var airportCityCode: String,
    var airportPicture: String,
    val airportContinent: String,
) : Parcelable
