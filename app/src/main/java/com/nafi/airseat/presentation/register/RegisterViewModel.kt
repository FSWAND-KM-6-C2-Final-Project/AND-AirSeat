package com.nafi.airseat.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.UserRepository
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun doRegister(
        fullname: String,
        email: String,
        phonenumber: String,
        confirmpassword: String,
        password: String,
    ) = repository
        .doRegister(
            fullName = fullname,
            email = email,
            phoneNumber = phonenumber,
            confirmPassword = confirmpassword,
            password = password,
        )
        .asLiveData(Dispatchers.IO)

    fun doVerifResendOtp(email: String) =
        repository
            .doVerifResendOtp(email)
            .asLiveData(Dispatchers.IO) // send otp to email
}
