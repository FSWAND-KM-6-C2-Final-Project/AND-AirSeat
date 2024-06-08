package com.nafi.airseat.data.source.local.pref

import android.content.SharedPreferences
import com.nafi.airseat.utils.SharedPreferenceUtils.set // Import the set operator function

interface CalendarPreference {
    fun isUsingCalendarMode(): Boolean

    fun setUsingCalendarMode(isUsingCalendarMode: Boolean)
}

class CalendarPreferenceImpl(private val pref: SharedPreferences) : CalendarPreference {
    override fun isUsingCalendarMode(): Boolean = pref.getBoolean(KEY_IS_USING_CALENDAR_MODE, false)

    override fun setUsingCalendarMode(isUsingCalendarMode: Boolean) {
        pref[KEY_IS_USING_CALENDAR_MODE] = isUsingCalendarMode
    }

    companion object {
        const val PREF_NAME = "airseat-pref"
        const val KEY_IS_USING_CALENDAR_MODE = "KEY_IS_USING_CALENDAR_MODE"
    }
}
