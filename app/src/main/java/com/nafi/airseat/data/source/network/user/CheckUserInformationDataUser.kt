package com.nafi.airseat.data.source.network.user

import com.google.gson.annotations.SerializedName

data class CheckUserInformationDataUser(
    @SerializedName("id")
    var id: Boolean?,
    @SerializedName("full_name")
    var full_name: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("phone_number")
    var phoneNumber: String?,
    @SerializedName("user_status")
    var userStatus: String?,
)
