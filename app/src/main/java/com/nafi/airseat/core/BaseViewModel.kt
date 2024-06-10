package com.nafi.airseat.core

import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.repository.UserPrefRepository

class BaseViewModel(
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {
    fun clearSession() {
        userPrefRepository.clearToken()
    }
}
