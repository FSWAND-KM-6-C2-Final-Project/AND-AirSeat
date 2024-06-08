package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportItemResponse
import com.nafi.airseat.data.source.network.service.AirSeatApiService

class AirportApiDataSource/*(private service: AirSeatApiService) : AirportDataSource {
    override suspend fun getAirportList(): List<AirportItemResponse> {
        return service.getAirports()
    }
}*/
