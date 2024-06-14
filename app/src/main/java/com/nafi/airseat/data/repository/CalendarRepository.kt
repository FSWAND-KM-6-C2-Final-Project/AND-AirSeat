package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.calendar.CalendarDataSource

interface CalendarRepository {
    fun isUsingCalendarMode(): Boolean

    fun setUsingCalendarMode(isUsingCalendarMode: Boolean)
}

class CalendarRepositoryImpl(private val dataSource: CalendarDataSource) : CalendarRepository {
    override fun isUsingCalendarMode(): Boolean {
        return dataSource.isUsingCalendarMode()
    }

    override fun setUsingCalendarMode(isUsingCalendarMode: Boolean) {
        dataSource.setUsingCalendarMode(isUsingCalendarMode)
    }
}
