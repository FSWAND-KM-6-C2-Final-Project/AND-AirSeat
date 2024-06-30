package com.c2.airseat.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    fun getHistoryData(
        bookingCode: String?,
        startDate: String?,
        endDate: String?,
    ) = repository.getHistoryData(bookingCode, startDate, endDate).asLiveData(Dispatchers.IO)
}
