package com.c2.airseat.data.source.network.model.booking

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VaNumber(
    @SerializedName("bank")
    val bank: String?,
    @SerializedName("va_number")
    val vaNumber: String?,
)
