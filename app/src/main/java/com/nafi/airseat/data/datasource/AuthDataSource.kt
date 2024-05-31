package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.User

interface AuthDataSource {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(
        email: String,
        password: String,
    ): Boolean

    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Boolean

    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun doVerif(
        email: String,
        code: String,
    ): Boolean

    suspend fun doVerifResendOtp(email: String): Boolean

    //reset password

    suspend fun reqChangePasswordByEmail(email: String): Boolean
    suspend fun reqChangePasswordByEmailResendOtp(email: String): Boolean
    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Boolean

    fun getCurrentUser(): User?

    fun isLoggedIn(): Boolean

    fun doLogout(): Boolean
}

class APIAuthDataSource(private val service: AuthService) : AuthDataSource {
    override suspend fun doLogin(
        email: String,
        password: String,
    ): Boolean {
        return service.doLogin(email, password)
    }

    override suspend fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        return service.doRegister(fullName, email, phoneNumber, password, confirmPassword)
    }

    override suspend fun doVerif(
        email: String,
        code: String,
    ): Boolean {
        return service.doVerif(email, code)
    }

    override suspend fun doVerifResendOtp(email: String): Boolean {
        return service.doVerifResendOtp(email)
    }

    //reset password
    override suspend fun reqChangePasswordByEmail(email: String): Boolean {
        return service.reqChangePasswordByEmail(email)
    }

    override suspend fun reqChangePasswordByEmailResendOtp(email: String): Boolean {
        return service.reqChangePasswordByEmailResendOtp(email)
    }

    override suspend fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        return service.verifChangePasswordOtp(code,email,password, confirmPassword)
    }

    override fun isLoggedIn(): Boolean {
        return service.isLoggedIn()
    }

    override fun doLogout(): Boolean {
        return service.doLogout()
    }

    override fun getCurrentUser(): User? {
        return service.getCurrentUser()
    }
}