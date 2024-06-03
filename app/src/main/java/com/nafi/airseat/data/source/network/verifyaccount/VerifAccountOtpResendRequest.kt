package com.nafi.airseat.data.source.network.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpResendRequest(
    @SerializedName("email")
    var email: String?,
)
