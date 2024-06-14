package com.nafi.airseat.data.datasource.flightdetail

import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailResponse

interface FlightDetailDataSource {
    suspend fun getFlightDetailList(id: String): FlightDetailResponse
}
