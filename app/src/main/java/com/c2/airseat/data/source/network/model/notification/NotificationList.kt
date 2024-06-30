package com.c2.airseat.data.source.network.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationList(
    @SerializedName("notification")
    val notification: List<NotificationData>?,
)
