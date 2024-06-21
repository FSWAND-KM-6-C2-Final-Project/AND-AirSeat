package com.nafi.airseat.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nafi.airseat.data.source.local.database.entity.AirportHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportHistoryDao {
    @Query("SELECT * FROM AIRPORTHISTORY")
    fun getAllAirportHistory(): Flow<List<AirportHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAirportHistory(searchHistory: AirportHistoryEntity): Long

    @Delete
    suspend fun deleteAirportHistory(searchHistory: AirportHistoryEntity): Int

    @Query("DELETE FROM AIRPORTHISTORY")
    suspend fun deleteAll()
}
