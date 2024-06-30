package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.UpdateProfile
import com.c2.airseat.data.source.network.model.profile.UpdateProfileResponse

fun UpdateProfileResponse?.toUpdateProfile() =
    UpdateProfile(
        status = this?.status.orEmpty(),
        message = this?.message.orEmpty(),
    )
