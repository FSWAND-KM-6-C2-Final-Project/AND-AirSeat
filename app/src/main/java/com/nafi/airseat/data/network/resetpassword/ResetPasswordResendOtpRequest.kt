package com.nafi.airseat.data.network.resetpassword

import com.google.gson.annotations.SerializedName

class ResetPasswordResendOtpRequest(
    @SerializedName("email")
    var email: String?,
)
