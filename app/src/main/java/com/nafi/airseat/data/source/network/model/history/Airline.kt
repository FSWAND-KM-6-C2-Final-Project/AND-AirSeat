package com.nafi.airseat.data.source.network.model.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Airline(
    @SerializedName("airline_name")
    val airlineName: String?,
    @SerializedName("airline_picture")
    val airlinePicture: String?
)