package com.nafi.airseat.data.source.network.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    var email: String?,
    @SerializedName("password")
    var password: String?,
)