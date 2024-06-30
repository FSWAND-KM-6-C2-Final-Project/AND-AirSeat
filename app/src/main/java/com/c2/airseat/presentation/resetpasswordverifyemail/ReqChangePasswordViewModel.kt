package com.c2.airseat.presentation.resetpasswordverifyemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ReqChangePasswordViewModel(private val repository: UserRepository) : ViewModel() {
    fun reqChangePasswordByEmail(email: String) =
        repository
            .reqChangePasswordByEmail(email)
            .asLiveData(Dispatchers.IO)
}
