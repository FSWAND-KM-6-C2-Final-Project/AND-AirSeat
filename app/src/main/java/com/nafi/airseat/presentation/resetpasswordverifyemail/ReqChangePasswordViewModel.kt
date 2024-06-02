package com.nafi.airseat.presentation.resetpasswordverifyemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ReqChangePasswordViewModel(private val repository: UserRepository) : ViewModel() {
    fun reqChangePasswordByEmailResendOtp(email: String) =
        repository
            .reqChangePasswordByEmailResendOtp(email)
            .asLiveData(Dispatchers.IO) // send otp to email
}
