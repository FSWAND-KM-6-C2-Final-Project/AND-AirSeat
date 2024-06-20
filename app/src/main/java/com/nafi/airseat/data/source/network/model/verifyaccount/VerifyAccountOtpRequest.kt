package com.nafi.airseat.data.source.network.model.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifyAccountOtpRequest(
    @SerializedName("email")
    var email: String?,
    @SerializedName("code")
    var code: String?,
)
