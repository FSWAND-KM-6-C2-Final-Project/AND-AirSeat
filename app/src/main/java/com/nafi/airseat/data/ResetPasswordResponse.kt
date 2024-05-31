package com.nafi.airseat.data

import com.google.gson.annotations.SerializedName
import com.nafi.airseat.data.network.ResetPasswordData

data class ResetPasswordResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
    @SerializedName("data")
    var data: ResetPasswordData,

    )
