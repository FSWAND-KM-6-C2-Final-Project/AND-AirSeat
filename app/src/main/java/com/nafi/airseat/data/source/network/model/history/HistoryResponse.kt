package com.nafi.airseat.data.source.network.model.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class HistoryResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("pagination")
    val pagination: Pagination?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("data")
    val data: Data
)