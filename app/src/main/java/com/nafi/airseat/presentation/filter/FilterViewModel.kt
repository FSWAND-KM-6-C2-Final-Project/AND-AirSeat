package com.nafi.airseat.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel() : ViewModel() {
    private val filterList = MutableLiveData<List<String>>()

    val filterItems: LiveData<List<String>> get() = filterList

    init {
        filterList.value =
            listOf(
                "Cheapest Price",
                "Shortest Duration",
                "Earliest - Departure",
                "Very End - Departure",
                "Earliest - Arrival",
                "Very End - Arrival",
            )
    }

    fun updateSeatClassList(newList: List<String>) {
        filterList.value = newList
    }
}
