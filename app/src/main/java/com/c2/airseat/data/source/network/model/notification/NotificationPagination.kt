package com.c2.airseat.data.source.network.model.notification

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationPagination(
    @SerializedName("pageNum")
    val pageNum: Int?,
    @SerializedName("pageSize")
    val pageSize: Int?,
    @SerializedName("totalData")
    val totalData: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?,
)
