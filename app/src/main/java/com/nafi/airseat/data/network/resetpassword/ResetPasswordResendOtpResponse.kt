package com.nafi.airseat.data.network.resetpassword

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.network.resetpassword.ResetPasswordResendOtpData

class ResetPasswordResendOtpResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
    @SerializedName("data")
    var data: ResetPasswordResendOtpData,
)
