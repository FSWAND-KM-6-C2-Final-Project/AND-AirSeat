package com.nafi.airseat.data.network.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpResendRequest(
    @SerializedName("email")
    var email: String?,
)
