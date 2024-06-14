package com.nafi.airseat.data.source.network.model.airport

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Pagination(
    @SerializedName("pageNum")
    val pageNum: Int?,
    @SerializedName("pageSize")
    val pageSize: Int?,
    @SerializedName("totalData")
    val totalData: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?,
)
