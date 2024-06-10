package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.seat.SeatDataSource
import com.nafi.airseat.data.mapper.toSeats
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

interface SeatRepository {
    fun getSeats(): Flow<ResultWrapper<List<Seat>>>
}

class SeatRepositoryImpl(private val dataSource: SeatDataSource) : SeatRepository {
    override fun getSeats(): Flow<ResultWrapper<List<Seat>>> {
        return proceedFlow { dataSource.getSeats().data.seats.toSeats() }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(1000)
            }
    }
}
