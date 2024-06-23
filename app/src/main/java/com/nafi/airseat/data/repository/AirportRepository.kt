package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.airport.AirportDataSource
import com.nafi.airseat.data.mapper.toAirports
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAirportByQuery(cityName: String?): Flow<ResultWrapper<List<Airport>>>
}

class AirportRepositoryImpl(private val dataSource: AirportDataSource) : AirportRepository {
    override fun getAirportByQuery(cityName: String?): Flow<ResultWrapper<List<Airport>>> {
        return proceedFlow { dataSource.getAirportByQuery(cityName).data?.airports.toAirports() }
    }
}
