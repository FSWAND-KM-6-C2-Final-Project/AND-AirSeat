package com.nafi.airseat.data.datasource.flight

import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class FlightApiDataSource(private val service: AirSeatApiService) : FlightDataSource {
    override suspend fun getFlightList(): FlightsResponse {
        val searchDate = ""
        val sortBy = ""
        val continent = ""
        val order = ""
        return service.getFlights(searchDate = searchDate, sortBy = sortBy, continent = continent, order = order)
    }
}
