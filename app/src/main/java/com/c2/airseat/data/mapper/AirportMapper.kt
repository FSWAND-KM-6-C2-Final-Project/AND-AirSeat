package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.Airport
import com.c2.airseat.data.source.network.model.airport.AirportDetail

fun AirportDetail?.toAirport() =
    Airport(
        id = this?.id ?: 0,
        airportName = this?.airportName.orEmpty(),
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
    )

fun Collection<AirportDetail>?.toAirports() =
    this?.map {
        it.toAirport()
    } ?: listOf()
