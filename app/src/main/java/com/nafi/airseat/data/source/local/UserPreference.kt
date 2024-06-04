package com.nafi.airseat.data.source.local

import android.content.Context
import com.nafi.airseat.utils.SharedPreferenceUtils

interface UserPreference {
    fun getToken(): String?

    fun saveToken(token: String)
}

class UserPreferenceImpl(private val context: Context) : UserPreference {
    private val pref = SharedPreferenceUtils.createPreference(context, PREF_NAME)

    override fun getToken(): String? = pref.getString(KEY_TOKEN, null)

    override fun saveToken(token: String) {
        pref.edit().putString(KEY_TOKEN, token).apply()
    }

    companion object {
        const val PREF_NAME = "airseat-pref"
        const val KEY_TOKEN = "KEY_TOKEN"
    }
}
