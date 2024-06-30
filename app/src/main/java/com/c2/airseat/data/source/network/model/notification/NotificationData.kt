package com.c2.airseat.data.source.network.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationData(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("notification_type")
    val notificationType: String?,
    @SerializedName("notification_title")
    val notificationTitle: String?,
    @SerializedName("notification_description")
    val notificationDescription: String?,
    @SerializedName("created_at")
    val updatedAt: String?,
)
