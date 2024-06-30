package com.c2.airseat.data.source.network.model.resetpassword

import com.google.gson.annotations.SerializedName

data class VerifyPasswordChangeOtpRequest(
    @SerializedName("code")
    var code: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("confirm_password")
    var confirmPassword: String?,
)
