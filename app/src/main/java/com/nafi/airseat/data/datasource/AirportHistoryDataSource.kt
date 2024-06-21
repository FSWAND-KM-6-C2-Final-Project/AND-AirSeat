package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.source.local.database.dao.AirportHistoryDao
import com.nafi.airseat.data.source.local.database.entity.AirportHistoryEntity
import kotlinx.coroutines.flow.Flow

interface AirportHistoryDataSource {
    fun getAllAirportHistory(): Flow<List<AirportHistoryEntity>>

    suspend fun addAirportHistory(airportHistory: AirportHistoryEntity): Long

    suspend fun deleteAirportHistory(airportHistory: AirportHistoryEntity): Int

    suspend fun deleteAll()
}

class AirportHistoryDataSourceImpl(private val dao: AirportHistoryDao) : AirportHistoryDataSource {
    override fun getAllAirportHistory(): Flow<List<AirportHistoryEntity>> = dao.getAllAirportHistory()

    override suspend fun addAirportHistory(airportHistory: AirportHistoryEntity): Long = dao.addAirportHistory(airportHistory)

    override suspend fun deleteAirportHistory(airportHistory: AirportHistoryEntity): Int = dao.deleteAirportHistory(airportHistory)

    override suspend fun deleteAll() = dao.deleteAll()
}
