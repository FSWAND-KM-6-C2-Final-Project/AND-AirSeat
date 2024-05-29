package com.nafi.airseat.data

import com.google.gson.annotations.SerializedName

data class VerifAccountOtpResendData(
    @SerializedName("email")
    var email: String?,
    @SerializedName("verification_user_resend_at")
    var verification_user_resend_at: String?,
    @SerializedName("verification_user_expired_at")
    var verification_user_expired_at: String?,
)
