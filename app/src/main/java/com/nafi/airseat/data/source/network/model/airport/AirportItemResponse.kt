package com.nafi.airseat.data.source.network.model.airport

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AirportItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("airport_name")
    val airportName: String?,
    @SerializedName("airport_city")
    val airportCity: String?,
    @SerializedName("airport_city_code")
    val airportCityCode: String?,
    @SerializedName("airport_picture")
    val airportPicture: String?,
    @SerializedName("airport_continent")
    val airportContinent: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
)
