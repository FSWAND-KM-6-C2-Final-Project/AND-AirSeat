package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.AirportDataSource
import com.c2.airseat.data.mapper.toAirports
import com.c2.airseat.data.model.Airport
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAirportByQuery(cityName: String?): Flow<ResultWrapper<List<Airport>>>
}

class AirportRepositoryImpl(private val dataSource: AirportDataSource) : AirportRepository {
    override fun getAirportByQuery(cityName: String?): Flow<ResultWrapper<List<Airport>>> {
        return proceedFlow { dataSource.getAirportByQuery(cityName).data?.airports.toAirports() }
    }
}
