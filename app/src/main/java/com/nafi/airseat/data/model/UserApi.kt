package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserApi(
    val status: Boolean,
    val message: String,
    val token: String,
) : Parcelable
