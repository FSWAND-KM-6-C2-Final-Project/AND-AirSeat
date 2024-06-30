package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.network.model.airport.AirportResponse
import com.c2.airseat.data.source.network.services.AirSeatApiService

interface AirportDataSource {
    suspend fun getAirportByQuery(cityName: String?): AirportResponse
}

class AirportApiDataSource(private val service: AirSeatApiService) : AirportDataSource {
    override suspend fun getAirportByQuery(cityName: String?): AirportResponse {
        return service.getAirportsByQuery(cityName)
    }
}
