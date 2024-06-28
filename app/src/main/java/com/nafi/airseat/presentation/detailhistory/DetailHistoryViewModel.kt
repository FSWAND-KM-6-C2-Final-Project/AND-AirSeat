package com.nafi.airseat.presentation.detailhistory

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.History
import com.nafi.airseat.data.repository.HistoryRepository

class DetailHistoryViewModel(
    private val extras: Bundle?,
    private val repository: HistoryRepository,
) : ViewModel() {
    private val detailHistoryData = extras?.getParcelable<History>(DetailHistoryActivity.EXTRAS_DETAIL_DATA)
}
