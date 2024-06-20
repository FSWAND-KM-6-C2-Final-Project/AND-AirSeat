package com.nafi.airseat.data.source.network.model.login

data class LoginResponse(
    var status: String,
    var message: String,
    var token: String,
)
