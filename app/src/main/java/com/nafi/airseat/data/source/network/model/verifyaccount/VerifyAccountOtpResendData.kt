package com.nafi.airseat.data.source.network.model.verifyaccount

import com.google.gson.annotations.SerializedName

data class VerifyAccountOtpResendData(
    @SerializedName("email")
    var email: String?,
    @SerializedName("verification_user_resend_at")
    var verification_user_resend_at: String?,
    @SerializedName("verification_user_expired_at")
    var verification_user_expired_at: String?,
)
