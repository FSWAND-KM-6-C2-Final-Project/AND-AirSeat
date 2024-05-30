package com.nafi.airseat.presentation.biodata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrdererBioViewModel : ViewModel() {
    private val _isFamilyNameMode = MutableLiveData(false)
    val isFamilyNameMode: LiveData<Boolean> get() = _isFamilyNameMode

    fun changeInputMode() {
        val currentValue = _isFamilyNameMode.value ?: false
        _isFamilyNameMode.value = !currentValue
    }
}
