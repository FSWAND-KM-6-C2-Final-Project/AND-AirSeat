package com.nafi.airseat.presentation.detailflight

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.repository.FlightDetailRepository
import kotlinx.coroutines.Dispatchers

class DetailFlightViewModel(
    val idExtras: Bundle?,
    private val repository: FlightDetailRepository,
) : ViewModel() {
    val id = idExtras?.getString(DetailFlightActivity.EXTRAS_ITEM)

    fun getDetailFlight(id: String) = repository.getFlightDetailList(id).asLiveData(Dispatchers.IO)
}
