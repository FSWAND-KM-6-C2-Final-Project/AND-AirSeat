package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportItemResponse

interface AirportDataSource {
    suspend fun getAirportList(): List<AirportItemResponse>
}
