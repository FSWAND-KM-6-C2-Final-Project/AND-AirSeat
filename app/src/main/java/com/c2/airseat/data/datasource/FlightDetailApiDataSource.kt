package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.network.model.flightdetail.FlightDetailResponse
import com.c2.airseat.data.source.network.services.AirSeatApiService

interface FlightDetailDataSource {
    suspend fun getFlightDetailList(id: String): FlightDetailResponse
}

class FlightDetailApiDataSource(private val service: AirSeatApiService) : FlightDetailDataSource {
    override suspend fun getFlightDetailList(id: String): FlightDetailResponse {
        return service.getFlightsDetail(id = id)
    }
}
