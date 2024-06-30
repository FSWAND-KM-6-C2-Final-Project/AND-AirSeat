package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.local.database.dao.SearchHistoryDao
import com.c2.airseat.data.source.local.database.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchHistoryDataSource {
    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>>

    suspend fun addSearchHistory(searchHistory: SearchHistoryEntity): Long

    suspend fun deleteSearchHistory(searchHistory: SearchHistoryEntity): Int

    suspend fun deleteAll()
}

class SearchHistoryDataSourceImpl(private val dao: SearchHistoryDao) : SearchHistoryDataSource {
    override fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>> = dao.getAllSearchHistory()

    override suspend fun addSearchHistory(searchHistory: SearchHistoryEntity): Long = dao.addSearchHistory(searchHistory)

    override suspend fun deleteSearchHistory(searchHistory: SearchHistoryEntity): Int = dao.deleteSearchHistory(searchHistory)

    override suspend fun deleteAll() = dao.deleteAll()
}
