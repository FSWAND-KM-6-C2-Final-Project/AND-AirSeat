package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.source.local.pref.UserPreference

interface UserPrefDataSource {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()
}

class UserPrefDataSourceImpl(private val userPreference: UserPreference) : UserPrefDataSource {
    override fun getToken(): String? {
        return userPreference.getToken()
    }

    override fun saveToken(token: String) {
        return userPreference.saveToken(token)
    }

    override fun clearToken() {
        return userPreference.cleaAll()
    }
}
