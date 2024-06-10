package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationModel(
    val id: Int,
    val notificationType: String,
    val notificationTitle: String,
    val notificationDescription: String,
    val updatedAt: String,
) : Parcelable
