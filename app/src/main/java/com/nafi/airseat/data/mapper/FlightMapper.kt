package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.Flight
import com.nafi.airseat.data.model.FlightAirline
import com.nafi.airseat.data.model.FlightAirportArrival
import com.nafi.airseat.data.model.FlightAirportDeparture
import com.nafi.airseat.data.source.network.model.flight.FlightAirlineResponse
import com.nafi.airseat.data.source.network.model.flight.FlightAirportArrivalResponse
import com.nafi.airseat.data.source.network.model.flight.FlightAirportDepartureResponse
import com.nafi.airseat.data.source.network.model.flight.FlightItemResponse

fun FlightItemResponse?.toFlight() =
    Flight(
        id = this?.id ?: 0,
        flightNumber = this?.flightNumber.orEmpty(),
        information = this?.information.orEmpty(),
        departureTime = this?.departureTime.orEmpty(),
        arrivalTime = this?.arrivalTime.orEmpty(),
        departureAirportId = this?.departureAirportId ?: 0,
        departureTerminal = this?.departureTerminal.orEmpty(),
        arrivalAirportId = this?.arrivalAirportId ?: 0,
        priceEconomy = this?.priceEconomy ?: 0,
        pricePremiumEconomy = this?.pricePremiumEconomy ?: 0,
        priceBusiness = this?.priceBusiness ?: 0,
        priceFirstClass = this?.priceFirstClass ?: 0,
        airlineId = this?.airlineId ?: 0,
        createdAt = this?.createdAt.orEmpty(),
        updatedAt = this?.updatedAt.orEmpty(),
        airline = this?.airline.toAirline(),
        departureAirport = this?.departureAirport.toAirportDeparture(),
        arrivalAirport = this?.arrivalAirport.toAirportArrival(),
    )

fun FlightAirlineResponse?.toAirline() =
    FlightAirline(
        airlineName = this?.airlineName.orEmpty(),
        airlinePicture = this?.airlinePicture.orEmpty(),
    )

fun FlightAirportDepartureResponse?.toAirportDeparture() =
    FlightAirportDeparture(
        airportName = this?.airportName.orEmpty(),
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
    )

fun FlightAirportArrivalResponse?.toAirportArrival() =
    FlightAirportArrival(
        airportName = this?.airportName.orEmpty(),
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
    )

fun Collection<FlightItemResponse>?.toFlights() =
    this?.map {
        it.toFlight()
    } ?: listOf()
