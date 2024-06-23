package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.AirportHistory
import com.nafi.airseat.data.source.local.database.entity.AirportHistoryEntity

fun AirportHistory?.toAirportHistoryEntity() =
    AirportHistoryEntity(
        id = this?.id ?: 0,
        airportHistory = this?.airportHistory.orEmpty(),
    )

fun AirportHistoryEntity?.toAirportHistory() =
    AirportHistory(
        id = this?.id ?: 0,
        airportHistory = this?.airportHistory.orEmpty(),
    )

fun List<AirportHistoryEntity>.toAirportHistoryList() = this.map { it.toAirportHistory() }
