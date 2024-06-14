package com.nafi.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Passenger(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("dob")
    val dob: String?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("identification_country")
    val identificationCountry: String?,
    @SerializedName("identification_expired")
    val identificationExpired: String?,
    @SerializedName("identification_number")
    val identificationNumber: String?,
    @SerializedName("identification_type")
    val identificationType: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
)
