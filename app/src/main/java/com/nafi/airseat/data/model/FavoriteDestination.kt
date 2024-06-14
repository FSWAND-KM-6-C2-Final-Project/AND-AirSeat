package com.nafi.airseat.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteDestination(
    var id: Int?,
    var img: String,
    var departDestination: String,
    var airline: String,
    var duration: String,
    var price: Int,
) : Parcelable
