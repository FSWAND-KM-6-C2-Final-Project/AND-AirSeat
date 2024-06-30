package com.c2.airseat.data.model

import com.c2.airseat.data.source.network.model.history.Pagination
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status")
    val status: ApiStatus?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("pagination")
    val pagination: Pagination?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("data")
    val data: T?,
)

enum class ApiStatus {
    @SerializedName("Success")
    Success,

    @SerializedName("Failed")
    Failed,

    @SerializedName("Error")
    Error,
}
