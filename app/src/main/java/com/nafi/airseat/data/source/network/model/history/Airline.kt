package com.nafi.airseat.data.source.network.model.history

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Airline(
    @SerializedName("airline_name")
    val airlineName: String?,
    @SerializedName("airline_picture")
    val airlinePicture: String?,
)
