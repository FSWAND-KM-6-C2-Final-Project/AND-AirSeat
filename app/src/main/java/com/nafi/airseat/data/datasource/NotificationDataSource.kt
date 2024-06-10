package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.source.network.model.notification.NotificationResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface NotificationDataSource {
    suspend fun getNotification(): NotificationResponse
}

class NotificationDataSourceImpl(private val apiService: AirSeatApiServiceWithAuthorization) :
    NotificationDataSource {
    override suspend fun getNotification(): NotificationResponse {
        return apiService.getNotification()
    }
}
