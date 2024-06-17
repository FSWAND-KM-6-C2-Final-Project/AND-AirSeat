package com.nafi.airseat.data.datasource.flight

import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService

class FlightApiDataSource(private val service: AirSeatApiService) : FlightDataSource {
    override suspend fun getFlightList(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        arrivalAirportId: String,
    ): FlightsResponse {
        return service.getFlights(
            searchDate = searchDateInput,
            sortBy = sortByClass,
            order = orderBy,
            deptAirport = departureAirportId,
            arrAirport = arrivalAirportId,
        )
    }
}
