package com.nafi.airseat.presentation.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ResetPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ) = repository
        .verifChangePasswordOtp(
            email = email,
            code = code,
            password = password,
            confirmPassword = confirmPassword,
        )
        .asLiveData(Dispatchers.IO)
}
