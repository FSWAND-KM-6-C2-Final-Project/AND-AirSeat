package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrdererBio(
    val fullName: String,
    val lastName: String?,
    val phoneNumber: String,
    val email: String,
) : Parcelable
