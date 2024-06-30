package com.c2.airseat.data.source.network.model.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateProfileResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
)
