package com.nafi.airseat.data.datasource.seat

import com.nafi.airseat.data.source.network.model.seat.SeatResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

class SeatApiDataSource(private val service: AirSeatApiServiceWithAuthorization) : SeatDataSource {
    override suspend fun getSeats(
        flightId: String,
        seatClass: String,
    ): SeatResponse {
        return service.getSeatData(flightId, seatClass)
    }
}
