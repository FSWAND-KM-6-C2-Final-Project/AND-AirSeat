package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.airport.AirportDataSource
import com.nafi.airseat.data.mapper.toAirports
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAirportList(): Flow<ResultWrapper<List<Airport>>>
}

class AirportRepositoryImpl(private val dataSource: AirportDataSource) : AirportRepository {
    override fun getAirportList(): Flow<ResultWrapper<List<Airport>>> {
        return proceedFlow { dataSource.getAirportList().data?.airports.toAirports() }
    }
}
