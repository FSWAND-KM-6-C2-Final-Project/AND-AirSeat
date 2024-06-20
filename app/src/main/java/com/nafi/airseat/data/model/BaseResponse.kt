package com.nafi.airseat.data.model

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.source.network.model.history.Pagination

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
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
