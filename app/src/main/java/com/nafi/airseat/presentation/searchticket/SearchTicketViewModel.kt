package com.nafi.airseat.presentation.searchticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nafi.airseat.data.model.AirportHistory
import com.nafi.airseat.data.repository.AirportHistoryRepository
import com.nafi.airseat.data.repository.AirportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchTicketViewModel(
    private val airportRepository: AirportRepository,
    private val repository: AirportHistoryRepository,
) : ViewModel() {
    fun getAirportData(cityName: String?) = airportRepository.getAirportByQuery(cityName).asLiveData(Dispatchers.IO)

    fun getAllAirportHistory() = repository.getAllAirportHistory().asLiveData(Dispatchers.IO)

    fun createAirportHistory(airportHistory: AirportHistory) = repository.createAirportHistory(airportHistory).asLiveData(Dispatchers.IO)

    fun removeAirportHistory(item: AirportHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAirportHistory(item).collect()
        }
    }

    fun deleteAllAirport() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll().collect()
        }
    }
}
