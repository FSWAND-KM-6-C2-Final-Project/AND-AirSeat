package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.source.local.UserPreference

interface UserDataSource {
    fun getToken(): String?

    fun saveToken(token: String)
}

class UserDataSourceImpl(private val userPreference: UserPreference) : UserDataSource {
    override fun getToken(): String? {
        return userPreference.getToken()
    }

    override fun saveToken(token: String) {
        userPreference.saveToken(token)
    }
}
