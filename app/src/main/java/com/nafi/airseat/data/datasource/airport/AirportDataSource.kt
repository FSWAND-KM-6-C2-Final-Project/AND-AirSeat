package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportResponse

interface AirportDataSource {
    suspend fun getAirportList(): AirportResponse
}
