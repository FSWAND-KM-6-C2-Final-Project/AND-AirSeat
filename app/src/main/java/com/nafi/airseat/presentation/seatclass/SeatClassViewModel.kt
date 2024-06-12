package com.nafi.airseat.presentation.seatclass

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.SeatClass
import com.nafi.airseat.data.repository.SeatClassRepository

class SeatClassViewModel(private val repository: SeatClassRepository) : ViewModel() {
    fun getSeatClass(): LiveData<List<SeatClass>> {
        return repository.getSeatClasses()
    }
}
