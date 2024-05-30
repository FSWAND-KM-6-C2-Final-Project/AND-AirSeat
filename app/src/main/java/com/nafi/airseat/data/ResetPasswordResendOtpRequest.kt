package com.nafi.airseat.data

import com.google.gson.annotations.SerializedName

class ResetPasswordResendOtpRequest(
    @SerializedName("email")
    var email: String?,
)
