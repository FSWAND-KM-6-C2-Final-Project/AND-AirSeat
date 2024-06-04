package com.nafi.airseat.data.source.network.model.register

import com.google.gson.annotations.SerializedName

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
