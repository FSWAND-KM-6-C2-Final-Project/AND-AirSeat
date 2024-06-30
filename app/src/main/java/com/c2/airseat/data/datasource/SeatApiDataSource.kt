package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.network.model.seat.SeatResponse
import com.c2.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface SeatDataSource {
    suspend fun getSeats(
        flightId: String,
        seatClass: String,
    ): SeatResponse
}

class SeatApiDataSource(private val service: AirSeatApiServiceWithAuthorization) : SeatDataSource {
    override suspend fun getSeats(
        flightId: String,
        seatClass: String,
    ): SeatResponse {
        return service.getSeatData(flightId, seatClass)
    }
}
