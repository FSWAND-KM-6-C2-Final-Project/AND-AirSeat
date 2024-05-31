package com.nafi.airseat.data.network

import com.google.gson.annotations.SerializedName

class ResetPasswordResendOtpRequest(
    @SerializedName("email")
    var email: String?,
)
