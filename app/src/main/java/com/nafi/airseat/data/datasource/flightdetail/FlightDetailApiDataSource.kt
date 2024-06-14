package com.nafi.airseat.data.datasource.flightdetail

import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class FlightDetailApiDataSource(private val service: AirSeatApiService) : FlightDetailDataSource {
    override suspend fun getFlightDetailList(id: String): FlightDetailResponse {
        return service.getFlightsDetail(id = id)
    }
}
