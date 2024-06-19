package com.nafi.airseat.data.model

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.source.network.model.history.Data
import com.nafi.airseat.data.source.network.model.history.Pagination

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
data class Response<T>(
    @SerializedName("status")
    val status: ApiStatus?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("pagination")
    val pagination: Pagination?,
    @SerializedName("requestAt")
    val requestAt: String?,
    @SerializedName("data")
    val data: Data,
)

enum class ApiStatus {
    @SerializedName("Success")
    Success,

    @SerializedName("Failed")
    Failed,

    @SerializedName("Error")
    Error,
}
