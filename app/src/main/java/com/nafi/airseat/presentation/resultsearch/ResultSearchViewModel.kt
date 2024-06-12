package com.nafi.airseat.presentation.resultsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.FlightRepository
import kotlinx.coroutines.Dispatchers

class ResultSearchViewModel(private val repository: FlightRepository) : ViewModel() {
    fun getFlightData() = repository.getFlights().asLiveData(Dispatchers.IO)
}
