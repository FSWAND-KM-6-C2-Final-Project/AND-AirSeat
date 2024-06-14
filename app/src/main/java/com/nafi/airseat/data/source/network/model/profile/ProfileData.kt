package com.nafi.airseat.data.source.network.model.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileData(
    @SerializedName("user")
    val user: ProfileUser,
)
