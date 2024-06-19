package com.nafi.airseat.presentation.searcthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nafi.airseat.data.model.SearchHistory
import com.nafi.airseat.data.repository.SearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchHistoryViewModel(private val repository: SearchHistoryRepository) : ViewModel() {
    fun getAllSearchHistory() = repository.getSearchHistory().asLiveData(Dispatchers.IO)

    fun createSearchHistory(searchHistory: SearchHistory) = repository.createHistory(searchHistory).asLiveData(Dispatchers.IO)

    fun removeSearchHistory(item: SearchHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSearchHistory(item).collect()
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll().collect()
        }
    }
}
