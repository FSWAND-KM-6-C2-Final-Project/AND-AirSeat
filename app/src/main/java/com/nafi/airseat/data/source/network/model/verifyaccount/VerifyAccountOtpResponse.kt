package com.nafi.airseat.data.source.network.model.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifyAccountOtpResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
)
