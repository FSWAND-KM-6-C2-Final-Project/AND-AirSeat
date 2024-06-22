package com.nafi.airseat.presentation.profilesetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers

class ProfileSettingViewModel(private val repository: ProfileRepository) : ViewModel() {
    fun deleteAccount() = repository.deleteAccount().asLiveData(Dispatchers.IO)
}
