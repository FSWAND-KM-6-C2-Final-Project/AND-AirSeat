package com.nafi.airseat.data.network.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("token")
    var token: String?,
)
