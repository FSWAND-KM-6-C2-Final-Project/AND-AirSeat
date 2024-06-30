package com.c2.airseat.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c2.airseat.data.repository.UserPrefRepository

class BaseViewModel(
    private val userPrefRepository: UserPrefRepository,
) : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token

    init {
        _token.value = userPrefRepository.getToken()
    }

    fun clearSession() {
        userPrefRepository.clearToken()
    }

    fun getToken() {
        _token.value = userPrefRepository.getToken()
    }
}
