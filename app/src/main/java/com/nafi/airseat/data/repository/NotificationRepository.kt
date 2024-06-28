package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.NotificationDataSource
import com.nafi.airseat.data.mapper.toNotificationList
import com.nafi.airseat.data.model.NotificationModel
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotification(): Flow<ResultWrapper<List<NotificationModel>>>
}

class NotificationRepositoryImpl(private val dataSource: NotificationDataSource) :
    NotificationRepository {
    override fun getNotification(): Flow<ResultWrapper<List<NotificationModel>>> {
        return proceedFlow { dataSource.getNotification().data?.notification.toNotificationList() }
    }
}
