package com.nafi.airseat.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.ProfileRepository
import com.nafi.airseat.data.repository.UserPrefRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val userPrefRepository: UserPrefRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    fun observeToken() = userPrefRepository.getToken()

    fun getDataProfile() = profileRepository.getDataProfile().asLiveData(Dispatchers.IO)
}
