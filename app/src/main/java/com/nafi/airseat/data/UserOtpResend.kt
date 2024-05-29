package com.nafi.airseat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserOtpResend(
    val email: String,
) : Parcelable
