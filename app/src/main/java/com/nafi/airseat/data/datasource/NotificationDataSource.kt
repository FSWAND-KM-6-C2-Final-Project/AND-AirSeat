package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.notification.NotificationList
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface NotificationDataSource {
    suspend fun getNotification(): BaseResponse<NotificationList>
}

class NotificationDataSourceImpl(private val apiService: AirSeatApiServiceWithAuthorization) :
    NotificationDataSource {
    override suspend fun getNotification(): BaseResponse<NotificationList> {
        return apiService.getNotification()
    }
}
