package com.nafi.airseat.data.datasource

interface UserDataSource {
    fun getToken(): String?

    fun saveToken(token: String)
}

class UserDataSourceImpl(private val dataSource: UserDataSource) : UserDataSource {
    override fun getToken(): String? {
        return dataSource.getToken()
    }

    override fun saveToken(token: String) {
        dataSource.saveToken(token)
    }
}
