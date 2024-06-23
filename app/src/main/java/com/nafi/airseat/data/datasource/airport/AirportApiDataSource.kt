package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class AirportApiDataSource(private val service: AirSeatApiService) : AirportDataSource {
    override suspend fun getAirportByQuery(cityName: String?): AirportResponse {
        return service.getAirportsByQuery(cityName)
    }
}
