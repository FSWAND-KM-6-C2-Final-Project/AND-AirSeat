package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.SearchHistoryDataSource
import com.c2.airseat.data.mapper.toSearchHistoryEntity
import com.c2.airseat.data.mapper.toSearchHistoryList
import com.c2.airseat.data.model.SearchHistory
import com.c2.airseat.data.source.local.database.entity.SearchHistoryEntity
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceed
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<ResultWrapper<List<SearchHistory>>>

    fun createHistory(searchHistory: SearchHistory): Flow<ResultWrapper<Boolean>>

    fun deleteSearchHistory(item: SearchHistory): Flow<ResultWrapper<Boolean>>

    fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class SearchHistoryRepositoryImpl(private val dataSource: SearchHistoryDataSource) :
    SearchHistoryRepository {
    override fun getSearchHistory(): Flow<ResultWrapper<List<SearchHistory>>> {
        return dataSource.getAllSearchHistory()
            .map {
                proceed {
                    it.toSearchHistoryList()
                }
            }.map {
                if (it.payload?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }.onStart {
                emit(ResultWrapper.Loading())
            }
    }

    override fun createHistory(searchHistory: SearchHistory): Flow<ResultWrapper<Boolean>> {
        return searchHistory.id.let { id ->
            proceedFlow {
                val affectedRow =
                    dataSource.addSearchHistory(
                        SearchHistoryEntity(
                            id = id,
                            searchHistory = searchHistory.searchHistory,
                        ),
                    )
                affectedRow > 0
            }
        }
    }

    override fun deleteSearchHistory(item: SearchHistory): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteSearchHistory(item.toSearchHistoryEntity()) == 0 }
    }

    override fun deleteAll(): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            dataSource.deleteAll()
            true
        }
    }
}
