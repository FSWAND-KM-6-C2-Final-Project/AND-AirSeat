package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.data.model.FlightDetailAirline
import com.nafi.airseat.data.model.FlightDetailArrival
import com.nafi.airseat.data.model.FlightDetailDeparture
import com.nafi.airseat.data.source.network.model.flightdetail.FlightDataDetail
import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailAirlineResponse
import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailArrivalResponse
import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailDepartureResponse

fun FlightDataDetail?.toDetailFlight() =
    FlightDetail(
        airlineId = this?.airlineId ?: 0,
        arrivalAirportId = this?.arrivalAirportId ?: 0,
        arrivalTime = this?.arrivalTime.orEmpty(),
        createdAt = this?.createdAt.orEmpty(),
        departureAirportId = this?.departureAirportId ?: 0,
        departureTerminal = this?.departureTerminal.orEmpty(),
        departureTime = this?.departureTime.orEmpty(),
        flightNumber = this?.flightNumber.orEmpty(),
        id = this?.id ?: 0,
        information = this?.information.orEmpty(),
        priceBusiness = this?.priceBusiness.orEmpty(),
        priceEconomy = this?.priceEconomy.orEmpty(),
        priceFirstClass = this?.priceFirstClass.orEmpty(),
        pricePremiumEconomy = this?.pricePremiumEconomy.orEmpty(),
        updatedAt = this?.updatedAt.orEmpty(),
        airline = this?.airline.toDetailAirline(),
        departureAirport = this?.departureAirport.toDetailDeparture(),
        arrivalAirport = this?.arrivalAirport.toDetailArrival(),
    )

fun FlightDetailAirlineResponse?.toDetailAirline() =
    FlightDetailAirline(
        airlineName = this?.airlineName.orEmpty(),
        airlinePicture = this?.airlinePicture.orEmpty(),
    )

fun FlightDetailDepartureResponse?.toDetailDeparture() =
    FlightDetailDeparture(
        airportName = this?.airportName.orEmpty(),
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
    )

fun FlightDetailArrivalResponse?.toDetailArrival() =
    FlightDetailArrival(
        airportName = this?.airportName.orEmpty(),
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
        airportContinent = this?.airportContinent.orEmpty(),
    )
