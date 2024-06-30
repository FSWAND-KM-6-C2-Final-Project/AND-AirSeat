package com.c2.airseat.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.c2.airseat.data.source.local.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM SEARCHHISTORY")
    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchHistory(searchHistory: SearchHistoryEntity): Long

    @Delete
    suspend fun deleteSearchHistory(searchHistory: SearchHistoryEntity): Int

    @Query("DELETE FROM SEARCHHISTORY")
    suspend fun deleteAll()
}
