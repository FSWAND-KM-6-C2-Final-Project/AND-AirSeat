package com.nafi.airseat.presentation.common.sharedviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.Airport
import com.nafi.airseat.data.model.FlightDetail

class SharedViewModel : ViewModel() {
    private val _selectedFlightDetail = MutableLiveData<FlightDetail>()
    val selectedFlightDetail: LiveData<FlightDetail> = _selectedFlightDetail

    fun setSelectedFlightDetail(flightDetail: FlightDetail) {
        _selectedFlightDetail.value = flightDetail
    }
}
