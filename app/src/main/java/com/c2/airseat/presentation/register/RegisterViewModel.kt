package com.c2.airseat.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        confirmPassword: String,
        password: String,
    ) = repository
        .doRegister(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber,
            confirmPassword = confirmPassword,
            password = password,
        )
        .asLiveData(Dispatchers.IO)
}
