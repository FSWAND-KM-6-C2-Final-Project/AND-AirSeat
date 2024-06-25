package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.HistoryDataSource
import com.nafi.airseat.data.mapper.toHistoryList
import com.nafi.airseat.data.model.History
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceed
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface HistoryRepository {
    fun getHistoryData(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): Flow<ResultWrapper<List<History>>>

    fun getHistoryDetail(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): Flow<ResultWrapper<Triple<List<History>, String, String>>>
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

    override fun getHistoryDetail(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): Flow<ResultWrapper<Triple<List<History>, String, String>>> {
        return proceedFlow { dataSource.getHistoryData(bookingCode, startDate = null, endDate = null).data?.booking.toHistoryList() }.map {
            proceed {
                val listHistory = it.payload.orEmpty()

                var amountPriceAdult = 0
                var amountPriceChild = 0

                for (history in listHistory) {
                    for (bookingDetail in history.bookingDetail) {
                        when (bookingDetail.passenger.passengerType) {
                            "Adult" -> amountPriceAdult += bookingDetail.price
                            "Child" -> amountPriceChild += bookingDetail.price
                        }
                    }
                }

                val amountPriceAdultString = amountPriceAdult.toString()
                val amountPriceChildString = amountPriceChild.toString()

                Triple(listHistory, amountPriceAdultString, amountPriceChildString)
            }
        }
    }
}
