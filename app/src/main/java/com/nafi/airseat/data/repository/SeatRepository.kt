package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.seat.SeatDataSource
import com.nafi.airseat.data.mapper.toSeats
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface SeatRepository {
    fun getSeats(flightId: String): Flow<ResultWrapper<List<Seat>>>
}

class SeatRepositoryImpl(private val dataSource: SeatDataSource) : SeatRepository {
    override fun getSeats(flightId: String): Flow<ResultWrapper<List<Seat>>> {
        return proceedFlow { dataSource.getSeats(flightId).data.seats.toSeats() }
    }
}
