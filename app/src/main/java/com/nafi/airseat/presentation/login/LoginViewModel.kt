package com.nafi.airseat.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.UserPrefRepository
import com.nafi.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    private val repository: UserRepository,
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {
    fun doLogin(
        email: String,
        password: String,
    ) = repository
        .doLogin(email, password)
        .asLiveData(Dispatchers.IO)

    fun saveToken(token: String) = userPrefRepository.saveToken(token)
}
