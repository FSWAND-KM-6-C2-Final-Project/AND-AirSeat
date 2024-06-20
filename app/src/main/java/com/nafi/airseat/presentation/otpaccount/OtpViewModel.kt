package com.nafi.airseat.presentation.otpaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class OtpViewModel(private val repository: UserRepository) : ViewModel() {
    fun doVerify(
        email: String,
        code: String,
    ) = repository
        .doVerify(
            email = email,
            code = code,
        )
        .asLiveData(Dispatchers.IO)

    fun doVerifyResendOtp(email: String) =
        repository
            .doVerifyResendOtp(email)
            .asLiveData(Dispatchers.IO)
}
