package com.c2.airseat.presentation.deleteaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers

class DeleteAccountViewModel(private val repository: ProfileRepository) : ViewModel() {
    fun deleteAccount() = repository.deleteAccount().asLiveData(Dispatchers.IO)
}
