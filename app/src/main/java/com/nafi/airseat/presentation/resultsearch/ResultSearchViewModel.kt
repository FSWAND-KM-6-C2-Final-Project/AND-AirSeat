package com.nafi.airseat.presentation.resultsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.FlightRepository
import kotlinx.coroutines.Dispatchers

class ResultSearchViewModel(private val repository: FlightRepository) : ViewModel() {
    // fun getFlightData() = repository.getFlights().asLiveData(Dispatchers.IO)
    /*private val departAirportId = MutableLiveData<Int>()
    private val destinationAirportId = MutableLiveData<Int>()

    fun setFlightQuery(departId: Int, destinationId: Int) {
        departAirportId.value = departId
        destinationAirportId.value = destinationId
    }

    fun getFlightData(): LiveData<Result<List<Flight>>> {
        return Transformations.switchMap(departAirportId) { departId ->
            Transformations.switchMap(destinationAirportId) { destinationId ->
                repository.getFlights(departId, destinationId).asLiveData(Dispatchers.IO)
            }
        }
    }*/
    fun getFlightData(
        searchDateInput: String,
        departureAirportId: String,
        destinationAirportId: String,
    ) = repository.getFlights(searchDateInput, departureAirportId, destinationAirportId).asLiveData(Dispatchers.IO)
}
