package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.NotificationModel
import com.c2.airseat.data.source.network.model.notification.NotificationData

fun NotificationData?.toNotificationModel() =
    NotificationModel(
        id = this?.id ?: 0,
        notificationType = this?.notificationType.orEmpty(),
        notificationTitle = this?.notificationTitle.orEmpty(),
        notificationDescription = this?.notificationDescription.orEmpty(),
        updatedAt = this?.updatedAt.orEmpty(),
    )

fun Collection<NotificationData>?.toNotificationList() =
    this?.map {
        it.toNotificationModel()
    } ?: listOf()
