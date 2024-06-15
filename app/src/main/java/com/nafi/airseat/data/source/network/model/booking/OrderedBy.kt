package com.nafi.airseat.data.source.network.model.booking

import com.google.gson.annotations.SerializedName

data class OrderedBy(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("email")
    val email: String,
)
