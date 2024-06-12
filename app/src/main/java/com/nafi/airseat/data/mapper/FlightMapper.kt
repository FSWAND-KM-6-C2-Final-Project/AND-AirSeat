package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.Flight
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
    )

fun Collection<FlightItemResponse>?.toFlights() =
    this?.map {
        it.toFlight()
    } ?: listOf()
