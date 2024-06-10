package com.nafi.airseat.data.source.local.pref

import android.content.SharedPreferences

interface UserPreference {
    fun getToken(): String?

    fun saveToken(token: String)

    fun cleaAll()
}

class UserPreferenceImpl(private val pref: SharedPreferences) : UserPreference {
    override fun getToken(): String? = pref.getString(KEY_TOKEN, null)

    override fun saveToken(token: String) {
        return pref.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun cleaAll() {
        pref.edit().clear().apply()
    }

    companion object {
        const val PREF_NAME = "airseat-pref"
        const val KEY_TOKEN = "KEY_TOKEN"
    }
}
