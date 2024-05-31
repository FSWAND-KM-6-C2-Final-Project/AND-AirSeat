package com.nafi.airseat.data.model

data class UserChangePassword(
    val code: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)
