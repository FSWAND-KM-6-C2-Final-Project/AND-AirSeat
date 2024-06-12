package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.flight.FlightDataSource
import com.nafi.airseat.data.mapper.toFlights
import com.nafi.airseat.data.model.Flight
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun getFlights(): Flow<ResultWrapper<List<Flight>>>
}

class FlightRepositoryImpl(private val dataSource: FlightDataSource) : FlightRepository {
    override fun getFlights(): Flow<ResultWrapper<List<Flight>>> {
        return proceedFlow {
            dataSource.getFlightList().data.flight.toFlights()
        }
    }
}
