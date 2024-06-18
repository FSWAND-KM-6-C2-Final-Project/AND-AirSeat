package com.nafi.airseat.presentation.common.sharedviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.FlightDetail

class SharedViewModel : ViewModel() {
    private val _selectedFlightDetail = MutableLiveData<FlightDetail?>()
    val selectedFlightDetail: MutableLiveData<FlightDetail?> = _selectedFlightDetail

    private val _returnFlightDetail = MutableLiveData<FlightDetail?>()
    val returnFlightDetail: MutableLiveData<FlightDetail?> = _returnFlightDetail

    fun setSelectedFlightDetail(flightDetail: FlightDetail) {
        _selectedFlightDetail.value = flightDetail
    }

    fun setReturnFlightDetail(flightDetail: FlightDetail) {
        _returnFlightDetail.value = flightDetail
    }

    fun clearSelectedFlightDetail() {
        _selectedFlightDetail.value = null
    }

    fun clearReturnFlightDetail() {
        _returnFlightDetail.value = null
    }

    fun isReturnTicketSelected(): Boolean {
        return _selectedFlightDetail.value != null
    }
}
