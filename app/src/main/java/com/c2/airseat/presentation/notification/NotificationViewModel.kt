package com.c2.airseat.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers

class NotificationViewModel(private val repository: NotificationRepository) : ViewModel() {
    fun getNotification() = repository.getNotification().asLiveData(Dispatchers.IO)
}
