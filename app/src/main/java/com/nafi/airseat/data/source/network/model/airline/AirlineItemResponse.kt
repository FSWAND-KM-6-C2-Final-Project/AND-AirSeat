package com.nafi.airseat.data.source.network.model.airline

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirlineItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("airline_name")
    val airlineName: String?,
    @SerializedName("airline_picture")
    val airlinePicture: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
)
