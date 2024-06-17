package com.nafi.airseat.data.datasource.flight

import com.nafi.airseat.data.source.network.model.flight.FlightsResponse

interface FlightDataSource {
    suspend fun getFlightList(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departureAirportId: String,
        arrivalAirportId: String,
    ): FlightsResponse
}
