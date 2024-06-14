package com.nafi.airseat.data.source.network.model.flight

import com.google.gson.annotations.SerializedName

data class PaginationResponse(
    @SerializedName("totalData")
    val totalData: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?,
    @SerializedName("pageNum")
    val pageNum: Int?,
    @SerializedName("pageSize")
    val pageSize: Int?,
)
