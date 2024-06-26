package com.nafi.airseat.presentation.seatbookreturn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.SeatRepository
import kotlinx.coroutines.Dispatchers

class SeatReturnViewModel(private val repository: SeatRepository) : ViewModel() {
    fun getFormattedSeatData(
        flightId: String,
        seatClassChoose: String,
    ) = repository.getFormattedSeatData(
        flightId,
        seatClassChoose,
    ).asLiveData(Dispatchers.IO)
}
