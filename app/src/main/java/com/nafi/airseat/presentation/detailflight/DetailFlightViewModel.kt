package com.nafi.airseat.presentation.detailflight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.FlightDetailRepository
import kotlinx.coroutines.Dispatchers

class DetailFlightViewModel(
    private val repository: FlightDetailRepository,
) : ViewModel() {
    fun getDetailFlight(id: String) = repository.getFlightDetailList(id).asLiveData(Dispatchers.IO)
}
