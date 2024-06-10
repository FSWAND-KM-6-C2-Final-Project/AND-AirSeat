package com.nafi.airseat.presentation.seatbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafi.airseat.data.model.Seat
import com.nafi.airseat.data.repository.SeatRepository
import com.nafi.airseat.utils.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SeatViewModel(private val repository: SeatRepository) : ViewModel() {
    private val _seats = MutableStateFlow<ResultWrapper<List<Seat>>>(ResultWrapper.Loading())
    val seats: StateFlow<ResultWrapper<List<Seat>>> get() = _seats

    init {
        fetchSeats()
    }

    private fun fetchSeats() {
        viewModelScope.launch {
            repository.getSeats().collect { result ->
                _seats.value = result
            }
        }
    }
}
