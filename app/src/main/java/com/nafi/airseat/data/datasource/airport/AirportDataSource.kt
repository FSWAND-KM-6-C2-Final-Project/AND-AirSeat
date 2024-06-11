package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportsResponse

interface AirportDataSource {
    suspend fun getAirportList(): AirportsResponse
}
