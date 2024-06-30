package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.SearchHistory
import com.c2.airseat.data.source.local.database.entity.SearchHistoryEntity

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
