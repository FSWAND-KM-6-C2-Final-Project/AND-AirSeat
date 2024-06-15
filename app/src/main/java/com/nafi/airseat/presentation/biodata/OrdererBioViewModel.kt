package com.nafi.airseat.presentation.biodata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.OrdererBio

class OrdererBioViewModel : ViewModel() {
    private val _isFamilyNameMode = MutableLiveData(false)
    val isFamilyNameMode: LiveData<Boolean> get() = _isFamilyNameMode

    private val _ordererBioData = MutableLiveData<OrdererBio>()

    fun saveOrdererBioData(
        fullName: String,
        phoneNumber: String,
        email: String,
        familyName: String,
    ) {
        val newOrdererBio =
            OrdererBio(
                fullName = fullName,
                lastName = familyName,
                phoneNumber = phoneNumber,
                email = email,
            )
        _ordererBioData.value = newOrdererBio
    }

    fun changeInputMode() {
        val currentValue = _isFamilyNameMode.value ?: false
        _isFamilyNameMode.value = !currentValue
    }

    private val _adultCount = MutableLiveData(0)
    val adultCount: LiveData<Int> get() = _adultCount

    private val _childCount = MutableLiveData(0)
    val childCount: LiveData<Int> get() = _childCount

    private val _babyCount = MutableLiveData(0)
    val babyCount: LiveData<Int> get() = _babyCount

    fun addAdult() {
        val count = _adultCount.value ?: 0
        _adultCount.value = count + 1
    }

    fun subAdult() {
        val count = _adultCount.value ?: 0
        if (count > 0) {
            _adultCount.value = count - 1
        }
    }

    fun addChild() {
        val count = _childCount.value ?: 0
        _childCount.value = count + 1
    }

    fun subChild() {
        val count = _childCount.value ?: 0
        if (count > 0) {
            _childCount.value = count - 1
        }
    }

    fun addBaby() {
        val count = _babyCount.value ?: 0
        _babyCount.value = count + 1
    }

    fun subBaby() {
        val count = _babyCount.value ?: 0
        if (count > 0) {
            _babyCount.value = count - 1
        }
    }
}
