package com.nafi.airseat.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    fun getNotification() = repository.getNotification().asLiveData(Dispatchers.IO)
}
