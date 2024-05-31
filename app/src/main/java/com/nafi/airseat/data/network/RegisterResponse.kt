package com.nafi.airseat.data.network

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.network.RegisterData

data class RegisterResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
    @SerializedName("data")
    var data: RegisterData,
)
