package com.nafi.airseat.presentation.searchticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.AirportRepository
import kotlinx.coroutines.Dispatchers

class SearchTicketViewModel(private val airportRepository: AirportRepository) : ViewModel() {
    fun getAirportData() = airportRepository.getAirportList().asLiveData(Dispatchers.IO)
}
