package com.nafi.airseat.presentation.otpresetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class OtpResetPasswordViewModel(private val repository: UserRepository) : ViewModel() {
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

    fun reqChangePasswordByEmailResendOtp(email: String) =
        repository
            .reqChangePasswordByEmailResendOtp(email)
            .asLiveData(Dispatchers.IO)
}
