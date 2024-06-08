package com.nafi.airseat.data.datasource.calendar

import com.nafi.airseat.data.source.local.pref.CalendarPreference

interface CalendarDataSource {
    fun isUsingCalendarMode(): Boolean

    fun setUsingCalendarMode(isUsingCalendarMode: Boolean)
}

class CalendarPreferenceDataSource(private val calendarPreference: CalendarPreference) : CalendarDataSource {
    override fun isUsingCalendarMode(): Boolean {
        return calendarPreference.isUsingCalendarMode()
    }

    override fun setUsingCalendarMode(isUsingCalendarMode: Boolean) {
        calendarPreference.setUsingCalendarMode(isUsingCalendarMode)
    }
}
