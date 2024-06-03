package com.nafi.airseat.data.source.network.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
)
