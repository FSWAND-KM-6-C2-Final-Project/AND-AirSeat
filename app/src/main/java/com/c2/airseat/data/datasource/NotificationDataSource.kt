package com.c2.airseat.data.datasource

import com.c2.airseat.data.model.BaseResponse
import com.c2.airseat.data.source.network.model.notification.NotificationList
import com.c2.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface NotificationDataSource {
    suspend fun getNotification(): BaseResponse<NotificationList>
}

class NotificationDataSourceImpl(private val apiService: AirSeatApiServiceWithAuthorization) :
    NotificationDataSource {
    override suspend fun getNotification(): BaseResponse<NotificationList> {
        return apiService.getNotification()
    }
}
