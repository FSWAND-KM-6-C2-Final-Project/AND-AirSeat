package com.nafi.airseat.presentation.home

import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.repository.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getCurrentUser() =
        userRepository
            .getCurrentUser()

    fun isUserLoggedOut() = userRepository.doLogout()
}
