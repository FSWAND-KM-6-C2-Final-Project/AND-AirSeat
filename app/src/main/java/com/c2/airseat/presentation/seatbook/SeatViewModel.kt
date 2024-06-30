package com.c2.airseat.presentation.seatbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.SeatRepository
import kotlinx.coroutines.Dispatchers

class SeatViewModel(private val repository: SeatRepository) : ViewModel() {
    fun getFormattedSeatData(
        flightId: String,
        seatClassChoose: String,
    ) = repository.getFormattedSeatData(
        flightId,
        seatClassChoose,
    ).asLiveData(Dispatchers.IO)
}
