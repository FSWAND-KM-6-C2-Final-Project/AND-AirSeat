package com.nafi.airseat.presentation.biodata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PassengerBioViewModel : ViewModel() {
    private val _isFamilyNameMode = MutableLiveData(false)

    val passengerBioItemList = mutableListOf<PassengerBioItem>()

    val isFamilyNameMode: LiveData<Boolean> get() = _isFamilyNameMode

    private val familyNameModes = mutableMapOf<Int, MutableLiveData<Boolean>>()

    fun setFamilyNameModeForPassenger(
        passengerIndex: Int,
        isChecked: Boolean,
    ) {
        if (!familyNameModes.containsKey(passengerIndex)) {
            familyNameModes[passengerIndex] = MutableLiveData(isChecked)
        } else {
            familyNameModes[passengerIndex]?.value = isChecked
        }
    }

    fun getFamilyNameModeForPassenger(passengerIndex: Int): LiveData<Boolean> {
        if (!familyNameModes.containsKey(passengerIndex)) {
            familyNameModes[passengerIndex] = MutableLiveData(false)
        }
        return familyNameModes[passengerIndex]!!
    }
}
