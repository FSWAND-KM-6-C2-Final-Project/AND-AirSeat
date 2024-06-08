package com.nafi.airseat.data.source.network.model.resetpassword

import com.google.gson.annotations.SerializedName

data class VerifyPasswordChangeOtpResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
)
