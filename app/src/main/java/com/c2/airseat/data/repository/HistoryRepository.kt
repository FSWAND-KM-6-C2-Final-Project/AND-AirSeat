package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.HistoryDataSource
import com.c2.airseat.data.mapper.toHistoryList
import com.c2.airseat.data.model.History
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryData(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): Flow<ResultWrapper<List<History>>>
}

class HistoryRepositoryImpl(private val dataSource: HistoryDataSource) : HistoryRepository {
    override fun getHistoryData(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): Flow<ResultWrapper<List<History>>> {
        return proceedFlow {
            dataSource.getHistoryData(bookingCode = bookingCode, startDate = startDate, endDate = endDate).data?.booking.toHistoryList()
        }
    }
}
