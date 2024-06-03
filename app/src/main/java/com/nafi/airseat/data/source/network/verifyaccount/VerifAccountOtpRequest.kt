package com.nafi.airseat.data.source.network.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpRequest(
    @SerializedName("email")
    var email: String?,
    @SerializedName("code")
    var code: String?,
)