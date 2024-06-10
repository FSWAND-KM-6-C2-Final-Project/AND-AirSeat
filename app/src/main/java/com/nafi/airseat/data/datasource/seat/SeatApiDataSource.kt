package com.nafi.airseat.data.datasource.seat

import com.nafi.airseat.data.network.seat.SeatResponse
import com.nafi.airseat.data.network.services.AirSeatApiService

class SeatApiDataSource(private val service: AirSeatApiService) : SeatDataSource {
    override suspend fun getSeats(): SeatResponse {
        return service.getSeats()
    }
}
