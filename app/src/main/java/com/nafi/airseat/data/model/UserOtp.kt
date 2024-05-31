package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserOtp(
    val email: String,
    val code: String,
) : Parcelable
