package com.c2.airseat.presentation.otpresetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class OtpResetPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    fun reqChangePasswordByEmailResendOtp(email: String) =
        repository
            .reqChangePasswordByEmailResendOtp(email)
            .asLiveData(Dispatchers.IO)
}
