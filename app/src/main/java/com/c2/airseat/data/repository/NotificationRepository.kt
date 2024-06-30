package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.NotificationDataSource
import com.c2.airseat.data.mapper.toNotificationList
import com.c2.airseat.data.model.NotificationModel
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
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
