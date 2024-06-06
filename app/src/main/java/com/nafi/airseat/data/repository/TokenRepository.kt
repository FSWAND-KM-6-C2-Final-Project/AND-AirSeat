package com.nafi.airseat.data.repository

import android.content.Context
import com.nafi.airseat.data.source.network.services.AirSeatApiService

interface TokenRepository {
    fun saveToken(token: String)

    fun getToken(): String?

    fun refreshToken(): String?
}

class TokenRepositoryImpl(private val context: Context, private val authService: AirSeatApiService) : TokenRepository {
    private val preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        preferences.edit().putString("token", token).apply()
    }

    override fun getToken(): String? {
        return preferences.getString("token", null)
    }

    override fun refreshToken(): String? {
        val response = authService.refreshToken().execute()
        return if (response.isSuccessful) {
            val newToken = response.body()?.token
            if (newToken != null) {
                saveToken(newToken)
            }
            newToken
        } else {
            null
        }
    }
}
