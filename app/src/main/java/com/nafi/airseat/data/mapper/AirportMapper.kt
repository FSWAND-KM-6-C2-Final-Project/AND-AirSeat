package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.data.source.network.model.airport.AirportItemResponse

fun AirportItemResponse?.toAirport() =
    Airport(
        id = this?.id,
        airportName = this?.airportName.orEmpty(),
        airportCity = this?.airportCity.orEmpty(),
        airportCityCode = this?.airportCityCode.orEmpty(),
        airportPicture = this?.airportPicture.orEmpty(),
    )

fun Collection<AirportItemResponse>?.toAirports() =
    this?.map {
        it.toAirport()
    } ?: listOf()
