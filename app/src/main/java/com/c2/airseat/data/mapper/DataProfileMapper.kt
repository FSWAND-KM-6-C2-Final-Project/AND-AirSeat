package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.DataProfile
import com.c2.airseat.data.source.network.model.profile.ProfileUser

fun ProfileUser?.toDataProfile() =
    DataProfile(
        id = this?.id ?: 0,
        fullName = this?.fullName.orEmpty(),
        email = this?.email.orEmpty(),
        phoneNumber = this?.phoneNumber.orEmpty(),
        userStatus = this?.userStatus.orEmpty(),
    )
