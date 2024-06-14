package com.nafi.airseat.presentation.searchticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.AirportRepository
import kotlinx.coroutines.Dispatchers

class SearchTicketViewModel(private val airportRepository: AirportRepository) : ViewModel() {
    fun getAirportData() = airportRepository.getAirportList().asLiveData(Dispatchers.IO)
    /*private val _airports = MutableLiveData<List<Airport>>()
    val airports: LiveData<List<Airport>>
        get() = _airports

    init {
        // Awalnya, tampilkan semua bandara
        airportRepository.value = emptyList()
    }

    fun filterAirports(query: String?) {
        val filteredList = mutableListOf<Airport>()
        if (!query.isNullOrBlank()) {
            // Jika query tidak kosong, filter bandara berdasarkan query
            getAirportData().forEach { airport ->
                if (airport.airportName.contains(query, ignoreCase = true)) {
                    filteredList.add(airport)
                }
            }
            _airports.value = filteredList
        } else {
            _airports.value = emptyList()
        }
    }

    fun clearAirports() {
        _airports.value = emptyList()
    }*/
}
