package com.nafi.airseat.data.datasource.flight

import com.nafi.airseat.data.source.network.model.flight.FlightItemResponse

interface FlightDataSource {
    suspend fun getFlightList(): List<FlightItemResponse>
}
