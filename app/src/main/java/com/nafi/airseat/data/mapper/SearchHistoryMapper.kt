package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.SearchHistory
import com.nafi.airseat.data.source.local.database.entity.SearchHistoryEntity

fun SearchHistory?.toSearchHistoryEntity() =
    SearchHistoryEntity(
        id = this?.id ?: 0,
        searchHistory = this?.searchHistory.orEmpty(),
    )

fun SearchHistoryEntity?.toSearchHistory() =
    SearchHistory(
        id = this?.id ?: 0,
        searchHistory = this?.searchHistory.orEmpty(),
    )

fun List<SearchHistoryEntity>.toSearchHistoryList() = this.map { it.toSearchHistory() }
