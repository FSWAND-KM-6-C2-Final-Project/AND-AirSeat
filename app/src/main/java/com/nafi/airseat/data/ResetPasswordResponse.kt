package com.nafi.airseat.data

import com.google.gson.annotations.SerializedName

class ResetPasswordResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
    @SerializedName("data")
    var data: ResetPasswordData,
)
