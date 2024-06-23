package com.nafi.airseat.data.source.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airportHistory")
data class AirportHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "airportHistory")
    var airportHistory: String? = null,
)
