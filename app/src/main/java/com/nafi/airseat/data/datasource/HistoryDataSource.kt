package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.history.HistoryData
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface HistoryDataSource {
    suspend fun getHistoryData(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): BaseResponse<HistoryData>
}

class HistoryDataSourceImpl(private val service: AirSeatApiServiceWithAuthorization) : HistoryDataSource {
    override suspend fun getHistoryData(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ): BaseResponse<HistoryData> {
        return service.getHistoryData(bookingCode = bookingCode, startDate = startDate, endDate = endDate)
    }
}
