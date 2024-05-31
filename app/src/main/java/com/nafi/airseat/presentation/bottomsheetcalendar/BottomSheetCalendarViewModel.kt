package com.nafi.airseat.presentation.bottomsheetcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class BottomSheetCalendarViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate> = _selectedDate

    init {
        // Initialize selected date to today
        _selectedDate.value = LocalDate.now()
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }
}
