package com.nafi.airseat.presentation.detailhistory

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.model.History
import com.nafi.airseat.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers

class DetailHistoryViewModel(
    private val extras: Bundle?,
    private val repository: HistoryRepository,
) : ViewModel() {
    private val detailHistoryData = extras?.getParcelable<History>(DetailHistoryActivity.EXTRAS_DETAIL_DATA)

    fun getDetailHistory(bookingCode: String?) = repository.getHistoryDetail(bookingCode).asLiveData(Dispatchers.IO)
}
