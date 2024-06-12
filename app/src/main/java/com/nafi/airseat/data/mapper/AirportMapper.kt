package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.data.source.network.model.airport.AirportDetail

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
