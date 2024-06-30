package com.c2.airseat.data.source.network.model.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileUser(
    @SerializedName("email")
    val email: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("user_status")
    val userStatus: String?,
)
