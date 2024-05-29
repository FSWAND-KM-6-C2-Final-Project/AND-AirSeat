package com.nafi.airseat.data

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpResendRequest(
    @SerializedName("email")
    var email: String?,
)
