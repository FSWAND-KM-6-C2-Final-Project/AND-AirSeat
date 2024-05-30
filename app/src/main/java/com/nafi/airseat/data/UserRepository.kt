package com.nafi.airseat.data

import com.nafi.airseat.data.datasource.AuthDataSource
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    @Throws(exceptionClasses = [Exception::class])
    fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>>

    @Throws(exceptionClasses = [java.lang.Exception::class])
    fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<Boolean>>

    @Throws(exceptionClasses = [java.lang.Exception::class])
    fun doVerif(
        email: String,
        code: String,
    ): Flow<ResultWrapper<Boolean>>

    fun doVerifResendOtp(email: String): Flow<ResultWrapper<Boolean>>

    //reset password
    fun reqChangePasswordByEmail(email: String): Flow<ResultWrapper<Boolean>>
    fun reqChangePasswordByEmailResendOtp(email: String):Flow<ResultWrapper<Boolean>>
    @Throws(exceptionClasses = [java.lang.Exception::class])
    fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<Boolean>>

    fun getCurrentUser(): User?

    fun isLoggedIn(): Boolean

    fun doLogout(): Boolean
}

class UserRepositoryImpl(private val dataSource: AuthDataSource) : UserRepository {
    override fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doLogin(email, password) }
    }

    override fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doRegister(fullName, email, phoneNumber, password, confirmPassword) }
    }

    override fun doVerif(
        email: String,
        code: String,
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doVerif(email, code) }
    }

    override fun doVerifResendOtp(email: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doVerifResendOtp(email) }
    }

    // reset password
    override fun reqChangePasswordByEmail(email: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.reqChangePasswordByEmail(email) }
    }

    override fun reqChangePasswordByEmailResendOtp(email: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.reqChangePasswordByEmailResendOtp(email) }
    }

    override fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.verifChangePasswordOtp(code, email, password, confirmPassword) }
    }

    override fun isLoggedIn(): Boolean {
        return dataSource.isLoggedIn()
    }

    override fun doLogout(): Boolean {
        return dataSource.doLogout()
    }

    override fun getCurrentUser(): User? {
        return dataSource.getCurrentUser()
    }
}
