package com.nafi.airseat.data.source.network.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("full_name")
    var fullName: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("phone_number")
    var phoneNumber: String?,
    @SerializedName("password")
    var password: String?,
    @SerializedName("confirm_password")
    var confirmPassword: String?,
)
