package com.nafi.airseat.data.source.network.model.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpResendRequest(
    @SerializedName("email")
    var email: String?,
)
