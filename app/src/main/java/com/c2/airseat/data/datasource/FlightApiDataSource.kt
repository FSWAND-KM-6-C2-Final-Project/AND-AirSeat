package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.network.model.flight.FlightsResponse
import com.c2.airseat.data.source.network.services.AirSeatApiService

interface FlightDataSource {
    suspend fun getFlightList(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        arrivalAirportId: String,
    ): FlightsResponse
}

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
