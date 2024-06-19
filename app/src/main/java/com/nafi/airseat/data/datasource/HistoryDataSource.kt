package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.source.network.model.history.HistoryResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface HistoryDataSource {
    suspend fun getHistoryData(bookingCode: String?): HistoryResponse
}

class HistoryDataSourceImpl(private val service: AirSeatApiServiceWithAuthorization) : HistoryDataSource {
    override suspend fun getHistoryData(bookingCode: String?): HistoryResponse {
        return service.getHistoryData(bookingCode = bookingCode)
    }
}
