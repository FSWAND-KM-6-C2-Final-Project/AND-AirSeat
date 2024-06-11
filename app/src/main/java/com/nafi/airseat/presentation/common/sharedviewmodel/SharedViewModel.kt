package com.nafi.airseat.presentation.common.sharedviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.Airport

class SharedViewModel : ViewModel() {
    private val _departAirport = MutableLiveData<Airport>()
    private val _destinationAirport = MutableLiveData<Airport>()

    val departAirport: LiveData<Airport> get() = _departAirport
    val destinationAirport: LiveData<Airport> get() = _destinationAirport

    fun setDepartAirport(airport: Airport) {
        _departAirport.value = airport
    }

    fun setDestinationAirport(airport: Airport) {
        _destinationAirport.value = airport
    }
}
