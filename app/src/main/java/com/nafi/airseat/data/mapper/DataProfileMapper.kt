package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.DataProfile
import com.nafi.airseat.data.source.network.model.profile.ProfileUser

fun ProfileUser?.toDataProfile() =
    DataProfile(
        id = this?.id ?: 0,
        fullName = this?.fullName.orEmpty(),
        email = this?.email.orEmpty(),
        phoneNumber = this?.phoneNumber.orEmpty(),
        userStatus = this?.userStatus.orEmpty(),
    )
