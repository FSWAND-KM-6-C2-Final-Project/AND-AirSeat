package com.nafi.airseat.data.datasource.seat

import com.nafi.airseat.data.network.seat.SeatResponse

interface SeatDataSource {
    suspend fun getSeats(): SeatResponse
}
