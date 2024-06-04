package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.UserDataSource

interface PreferenceRepository {
    fun getToken(): String?

    fun saveToken(token: String)
}

class PreferenceRepositoryImpl(private val dataSource: UserDataSource) : PreferenceRepository {
    override fun getToken(): String? {
        return dataSource.getToken()
    }

    override fun saveToken(token: String) {
        dataSource.saveToken(token)
    }
}
