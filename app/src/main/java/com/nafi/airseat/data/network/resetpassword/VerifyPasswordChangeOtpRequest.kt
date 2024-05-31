package com.nafi.airseat.data.network.resetpassword

import com.google.gson.annotations.SerializedName

data class VerifyPasswordChangeOtpRequest(
    @SerializedName("email")
    var email: String?,
    @SerializedName("code")
    var code: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("confirm_password")
    var confirmPassword: String?,
)
