package com.c2.airseat.data.source.local.pref

import android.content.SharedPreferences
import com.c2.airseat.utils.SharedPreferenceUtils.set

interface UserPreference {
    fun getToken(): String?

    fun saveToken(token: String)

    fun cleaAll()

    fun isAppIntroShown(): Boolean

    fun setAppIntroShown(isShown: Boolean)
}

class UserPreferenceImpl(private val pref: SharedPreferences) : UserPreference {
    override fun getToken(): String? = pref.getString(KEY_TOKEN, null)

    override fun saveToken(token: String) {
        return pref.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun cleaAll() {
        pref.edit().clear().apply()
    }

    override fun isAppIntroShown(): Boolean = pref.getBoolean(KEY_APP_INTRO_SHOWN, false)

    override fun setAppIntroShown(isShown: Boolean) {
        pref[KEY_APP_INTRO_SHOWN] = isShown
    }

    companion object {
        const val PREF_NAME = "airseat-pref"
        const val KEY_TOKEN = "KEY_TOKEN"
        const val KEY_APP_INTRO_SHOWN = "KEY_APP_INTRO_SHOWN"
    }
}
