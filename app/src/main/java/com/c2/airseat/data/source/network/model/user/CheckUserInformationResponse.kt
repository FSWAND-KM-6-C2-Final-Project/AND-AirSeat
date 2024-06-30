package com.c2.airseat.data.source.network.model.user

import com.google.gson.annotations.SerializedName

data class CheckUserInformationResponse(
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("requestAt")
    var requestAt: String?,
    @SerializedName("data")
    var data: CheckUserInformationData,
)
