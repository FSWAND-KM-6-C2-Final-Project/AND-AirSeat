package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.UserPrefDataSource

interface UserPrefRepository {
    fun saveToken(token: String)

    fun getToken(): String?

    fun cleatToken()
}

class UserPrefRepositoryImpl(private val userPrefDataSource: UserPrefDataSource) :
    UserPrefRepository {
    override fun saveToken(token: String) {
        return userPrefDataSource.saveToken(token)
    }

    override fun getToken(): String? {
        return userPrefDataSource.getToken()
    }

    override fun cleatToken() {
        return userPrefDataSource.clearToken()
    }
}
