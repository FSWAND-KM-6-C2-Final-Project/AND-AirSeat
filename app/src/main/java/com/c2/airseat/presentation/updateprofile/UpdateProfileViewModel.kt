package com.c2.airseat.presentation.updateprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.ProfileRepository
import com.c2.airseat.data.source.network.model.profile.UpdateProfileRequest
import kotlinx.coroutines.Dispatchers

class UpdateProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    fun getUpdateProfile(updateProfileRequest: UpdateProfileRequest) =
        repository.updateProfileData(updateProfileRequest).asLiveData(Dispatchers.IO)
}
