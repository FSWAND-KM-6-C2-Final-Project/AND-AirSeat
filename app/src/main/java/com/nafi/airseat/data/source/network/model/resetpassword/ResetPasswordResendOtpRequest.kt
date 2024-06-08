package com.nafi.airseat.data.source.network.model.resetpassword

import com.google.gson.annotations.SerializedName

class ResetPasswordResendOtpRequest(
    @SerializedName("email")
    var email: String?,
)
