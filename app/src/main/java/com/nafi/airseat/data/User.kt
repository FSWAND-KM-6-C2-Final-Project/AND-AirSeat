package com.nafi.airseat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String,
    val token: String,
) : Parcelable
