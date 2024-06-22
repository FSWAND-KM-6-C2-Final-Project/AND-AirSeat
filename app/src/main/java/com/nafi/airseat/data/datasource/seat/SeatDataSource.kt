package com.nafi.airseat.data.datasource.seat

import com.nafi.airseat.data.source.network.model.seat.SeatResponse

interface SeatDataSource {
    suspend fun getSeats(flightId: String): SeatResponse
}
