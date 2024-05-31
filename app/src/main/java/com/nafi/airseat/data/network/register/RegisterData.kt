package com.nafi.airseat.data.network.register

import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("email")
    var email: String?,
    @SerializedName("verification_user_resend_at")
    var verification_user_resend_at: String?,
    @SerializedName("verification_user_expired_at")
    var verification_user_expired_at: String?,
)
