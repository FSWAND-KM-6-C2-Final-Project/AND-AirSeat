package com.nafi.airseat.presentation.updateprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.ProfileRepository
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import kotlinx.coroutines.Dispatchers

class UpdateProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    fun getUpdateProfile(updateProfileRequest: UpdateProfileRequest) =
        repository.updateProfileData(updateProfileRequest).asLiveData(Dispatchers.IO)
}
