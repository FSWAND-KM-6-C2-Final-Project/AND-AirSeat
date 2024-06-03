package com.nafi.airseat.data.source.network.resetpassword

import com.google.gson.annotations.SerializedName

class ResetPasswordResendOtpData(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("reset_password_resend_at")
    var reset_password_resend_at: String?,
    @SerializedName("reset_password_expired_at")
    var reset_password_expired_at: String?,
)
