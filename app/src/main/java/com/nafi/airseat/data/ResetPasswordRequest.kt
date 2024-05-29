package com.nafi.airseat.data

import com.google.gson.annotations.SerializedName

class ResetPasswordRequest(
    @SerializedName("email")
    var email: String?,
)
