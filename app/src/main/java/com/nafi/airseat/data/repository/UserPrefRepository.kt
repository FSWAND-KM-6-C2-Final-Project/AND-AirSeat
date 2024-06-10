package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.UserPrefDataSource

interface UserPrefRepository {
    fun saveToken(token: String)

    fun getToken(): String?

    fun clearToken()
}

class UserPrefRepositoryImpl(private val userPrefDataSource: UserPrefDataSource) :
    UserPrefRepository {
    override fun saveToken(token: String) {
        return userPrefDataSource.saveToken(token)
    }

    override fun getToken(): String? {
        return userPrefDataSource.getToken()
    }

    override fun clearToken() {
        return userPrefDataSource.clearToken()
    }
}
