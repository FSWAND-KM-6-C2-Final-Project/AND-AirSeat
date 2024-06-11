package com.nafi.airseat.presentation.passengers

import androidx.lifecycle.ViewModel

class PassengersViewModel : ViewModel() {
    var adultCount = 1 // Default count of adult passengers
        private set

    var childCount = 0 // Default count of child passengers
        private set

    var babyCount = 0 // Default count of baby passengers
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

    // Decrement methods
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

    // Method to get total passenger count
    fun getTotalPassengerCount(): Int {
        return adultCount + childCount + babyCount
    }
}
