package com.c2.airseat.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    private val filterList = MutableLiveData<List<String>>()

    val filterItems: LiveData<List<String>> get() = filterList

    init {
        filterList.value =
            listOf(
                "Cheapest Price",
                "Highest Price",
                "Earliest - Departure",
                "Very End - Departure",
            )
    }
}
