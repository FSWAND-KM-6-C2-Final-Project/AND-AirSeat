package com.nafi.airseat.data

import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    @Throws(exceptionClasses = [Exception::class])
    fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>>

    fun getCurrentUser(): User?

    fun isLoggedIn(): Boolean
}

class UserRepositoryImpl(private val dataSource: AuthDataSource) : UserRepository {
    override fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doLogin(email, password) }
    }

    override fun isLoggedIn(): Boolean {
        return dataSource.isLoggedIn()
    }

    override fun getCurrentUser(): User? {
        return dataSource.getCurrentUser()
    }
}
