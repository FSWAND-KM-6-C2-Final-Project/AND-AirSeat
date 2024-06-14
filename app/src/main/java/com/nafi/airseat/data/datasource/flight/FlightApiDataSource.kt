package com.nafi.airseat.data.datasource.flight

import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class FlightApiDataSource(private val service: AirSeatApiService) : FlightDataSource {
    override suspend fun getFlightList(
        searchDateInput: String,
        departureAirportId: String,
        arrivalAirportId: String,
    ): FlightsResponse {
        val sortBy = ""
        val order = ""
        return service.getFlights(
            searchDate = searchDateInput,
            sortBy = sortBy,
            order = order,
            deptAirport = departureAirportId,
            arrAirport = arrivalAirportId,
        )
    }
}
