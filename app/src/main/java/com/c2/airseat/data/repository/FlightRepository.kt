package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.FlightDataSource
import com.c2.airseat.data.mapper.toFlights
import com.c2.airseat.data.model.Flight
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun getFlights(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departAirportId: String,
        destinationAirportId: String,
    ): Flow<ResultWrapper<List<Flight>>>
}

class FlightRepositoryImpl(private val dataSource: FlightDataSource) : FlightRepository {
    override fun getFlights(
        searchDateInput: String,
        sortByClass: String,
        orderBy: String,
        departAirportId: String,
        destinationAirportId: String,
    ): Flow<ResultWrapper<List<Flight>>> {
        return proceedFlow {
            dataSource.getFlightList(searchDateInput, sortByClass, orderBy, departAirportId, destinationAirportId).data.flight.toFlights()
        }
    }
}
