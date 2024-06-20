package com.nafi.airseat.data.datasource.seat

import com.nafi.airseat.data.source.network.model.seat.SeatResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class SeatApiDataSource(private val service: AirSeatApiService) : SeatDataSource {
    override suspend fun getSeats(flightId: String): SeatResponse {
        return service.getSeatData(flightId)
    }
}
