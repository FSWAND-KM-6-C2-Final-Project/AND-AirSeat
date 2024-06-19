package com.nafi.airseat.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    fun getHistoryData(bookingCode: String?) = repository.getHistoryData(bookingCode = bookingCode).asLiveData(Dispatchers.IO)
}
