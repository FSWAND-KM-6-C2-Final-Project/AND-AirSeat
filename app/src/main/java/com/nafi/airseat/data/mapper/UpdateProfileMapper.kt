package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.UpdateProfile
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileResponse

fun UpdateProfileResponse?.toUpdateProfile() =
    UpdateProfile(
        status = this?.status.orEmpty(),
        message = this?.message.orEmpty(),
    )
