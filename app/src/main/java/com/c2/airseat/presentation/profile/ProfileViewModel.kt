package com.c2.airseat.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {
    fun getDataProfile() = profileRepository.getDataProfile().asLiveData(Dispatchers.IO)
}
