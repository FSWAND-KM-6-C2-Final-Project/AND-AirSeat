package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.FlightDetailDataSource
import com.c2.airseat.data.mapper.toDetailFlight
import com.c2.airseat.data.model.FlightDetail
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface FlightDetailRepository {
    fun getFlightDetailList(id: String): Flow<ResultWrapper<FlightDetail>>
}

class FlightDetailRepositoryImpl(private val flightDetailDataSource: FlightDetailDataSource) : FlightDetailRepository {
    override fun getFlightDetailList(id: String): Flow<ResultWrapper<FlightDetail>> {
        return proceedFlow {
            flightDetailDataSource.getFlightDetailList(id).data.flight.toDetailFlight()
        }
    }
}
