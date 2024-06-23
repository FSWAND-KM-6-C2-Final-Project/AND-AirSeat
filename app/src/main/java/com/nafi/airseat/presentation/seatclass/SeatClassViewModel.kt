package com.nafi.airseat.presentation.seatclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeatClassViewModel : ViewModel() {
    private val seatClassList = MutableLiveData<List<String>>()

    val seatClassItems: LiveData<List<String>> get() = seatClassList

    init {
        seatClassList.value = listOf("Economy", "Premium Economy", "Business", "First Class")
    }
}
