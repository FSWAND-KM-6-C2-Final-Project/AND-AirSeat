package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportsResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class AirportApiDataSource(private val service: AirSeatApiService) : AirportDataSource {
    override suspend fun getAirportList(): AirportsResponse {
        return service.getAirports()
    }
}
