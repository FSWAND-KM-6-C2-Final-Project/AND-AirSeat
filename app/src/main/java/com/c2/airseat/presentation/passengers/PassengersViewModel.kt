package com.c2.airseat.presentation.passengers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PassengersViewModel : ViewModel() {
    var adultCount = 0
        private set

    var childCount = 0
        private set

    var babyCount = 0
        private set

    private val _showWarning = MutableLiveData<String>()
    val showWarning: LiveData<String> get() = _showWarning

    fun incrementAdultCount() {
        adultCount++
    }

    fun incrementChildCount() {
        childCount++
    }

    fun incrementBabyCount() {
        if (babyCount < adultCount) {
            babyCount++
        } else {
            _showWarning.value = "The number of babies should not exceed the number of adults"
        }
    }

    fun decrementAdultCount() {
        if (adultCount > 1) {
            adultCount--
        }
    }

    fun decrementChildCount() {
        if (childCount > 0) {
            childCount--
        }
    }

    fun decrementBabyCount() {
        if (babyCount > 0) {
            babyCount--
        }
    }

    fun getTotalPassengerCount(): Int {
        return adultCount + childCount + babyCount
    }
}
