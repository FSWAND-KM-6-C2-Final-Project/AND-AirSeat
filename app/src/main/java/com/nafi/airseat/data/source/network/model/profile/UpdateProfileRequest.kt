package com.nafi.airseat.data.source.network.model.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UpdateProfileRequest(
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
)
