package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.HistoryDataSource
import com.nafi.airseat.data.mapper.toHistoryList
import com.nafi.airseat.data.model.History
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
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
