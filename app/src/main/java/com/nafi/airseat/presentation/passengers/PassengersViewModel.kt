package com.nafi.airseat.presentation.passengers

import androidx.lifecycle.ViewModel

class PassengersViewModel : ViewModel() {
    var adultCount = 0
        private set

    var childCount = 0
        private set

    var babyCount = 0
        private set

    // Increment methods
    fun incrementAdultCount() {
        adultCount++
    }

    fun incrementChildCount() {
        childCount++
    }

    fun incrementBabyCount() {
        babyCount++
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
