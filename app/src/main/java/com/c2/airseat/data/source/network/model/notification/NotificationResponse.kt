package com.c2.airseat.data.source.network.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("pagination")
    val pagination: NotificationPagination?,
    @SerializedName("data")
    val data: NotificationList,
)
