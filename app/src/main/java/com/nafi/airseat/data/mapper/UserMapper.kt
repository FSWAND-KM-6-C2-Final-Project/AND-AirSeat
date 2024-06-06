package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.UserApi
import com.nafi.airseat.data.source.network.model.login.LoginResponse

fun LoginResponse?.toUser() =
    UserApi(
        status = this?.status.orEmpty(),
        message = this?.message.orEmpty(),
        token = this?.token.orEmpty(),
    )

fun Collection<LoginResponse>?.toUsers() =
    this?.map {
        it.toUser()
    } ?: listOf()
