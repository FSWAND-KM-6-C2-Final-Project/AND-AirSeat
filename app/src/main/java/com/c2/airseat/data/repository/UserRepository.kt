package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.AuthDataSource
import com.c2.airseat.data.source.network.model.login.LoginRequest
import com.c2.airseat.data.source.network.model.register.RegisterRequest
import com.c2.airseat.data.source.network.model.resetpassword.ResetPasswordRequest
import com.c2.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpRequest
import com.c2.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpRequest
import com.c2.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpRequest
import com.c2.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendRequest
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<String>>

    fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<String>>

    fun doVerify(
        email: String,
        code: String,
    ): Flow<ResultWrapper<String>>

    fun doVerifyResendOtp(email: String): Flow<ResultWrapper<String>>

    // reset password
    fun reqChangePasswordByEmail(email: String): Flow<ResultWrapper<String>>

    fun reqChangePasswordByEmailResendOtp(email: String): Flow<ResultWrapper<String>>

    fun verifyChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<String>>
}

class UserRepositoryImpl(private val dataSource: AuthDataSource) : UserRepository {
    override fun doLogin(
        email: String,
        password: String,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow { dataSource.doLogin(LoginRequest(email, password)).token.orEmpty() }
    }

    override fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.doRegister(
                RegisterRequest(
                    fullName,
                    email,
                    phoneNumber,
                    password,
                    confirmPassword,
                ),
            ).message.orEmpty()
        }
    }

    override fun doVerify(
        email: String,
        code: String,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.doVerify(
                VerifyAccountOtpRequest(
                    email,
                    code,
                ),
            ).message.orEmpty()
        }
    }

    override fun doVerifyResendOtp(email: String): Flow<ResultWrapper<String>> {
        return proceedFlow { dataSource.doVerifyResendOtp(VerifyAccountOtpResendRequest(email)).message.orEmpty() }
    }

    override fun reqChangePasswordByEmail(email: String): Flow<ResultWrapper<String>> {
        return proceedFlow { dataSource.reqChangePasswordByEmail(ResetPasswordRequest(email)).message.orEmpty() }
    }

    override fun reqChangePasswordByEmailResendOtp(email: String): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.reqChangePasswordByEmailResendOtp(
                ResetPasswordResendOtpRequest(email),
            ).message.orEmpty()
        }
    }

    override fun verifyChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            dataSource.verifyChangePasswordOtp(
                VerifyPasswordChangeOtpRequest(
                    code,
                    email,
                    password,
                    confirmPassword,
                ),
            ).message.orEmpty()
        }
    }
}
