package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.flightdetail.FlightDetailDataSource
import com.nafi.airseat.data.mapper.toDetailFlight
import com.nafi.airseat.data.model.FlightDetail
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
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
