package com.nafi.airseat.data.source.network.model.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    var email: String?,
    @SerializedName("password")
    var password: String?,
)
