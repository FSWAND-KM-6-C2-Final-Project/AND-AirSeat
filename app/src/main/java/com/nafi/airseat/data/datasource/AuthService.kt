package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.User
import com.nafi.airseat.data.model.UserChangePassword
import com.nafi.airseat.data.model.UserOtp
import com.nafi.airseat.data.model.UserOtpResend
import com.nafi.airseat.data.source.network.login.LoginRequest
import com.nafi.airseat.data.source.network.register.RegisterRequest
import com.nafi.airseat.data.source.network.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.source.network.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.source.network.services.AirSeatApiService
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpRequest
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResendRequest

interface AuthService {
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

    // reset password
    suspend fun reqChangePasswordByEmail(email: String): Boolean

    suspend fun reqChangePasswordByEmailResendOtp(email: String): Boolean

    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Boolean

    fun isLoggedIn(): Boolean

    fun getCurrentUser(): User?

    fun doLogout(): Boolean
}

class AuthServiceImpl(private val apiService: com.nafi.airseat.data.source.network.services.AirSeatApiService) : AuthService {
    private var currentUser: User? = null
    private var otpUser: UserOtp? = null
    private var otpUserResend: UserOtpResend? = null
    private var userChangePassword: UserChangePassword? = null

    override suspend fun doLogin(
        email: String,
        password: String,
    ): Boolean {
        val loginRequest = com.nafi.airseat.data.source.network.login.LoginRequest(email, password)
        val response = apiService.login(loginRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            currentUser = User(email = email, token = response.body()?.token ?: "")
            true
        } else {
            false
        }
    }

    override suspend fun doRegister(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        val registerRequest = com.nafi.airseat.data.source.network.register.RegisterRequest(
            fullName,
            email,
            phoneNumber,
            password,
            confirmPassword
        )
        val response = apiService.register(registerRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            currentUser = User(email = email, token = response.body()?.requestAt ?: "") // bingung
            true
        } else {
            false
        }
    }

    override suspend fun doVerif(
        email: String,
        code: String,
    ): Boolean {
        val verifAccountOtpRequest = VerifAccountOtpRequest(email, code)
        val response = apiService.verifAccountOtp(verifAccountOtpRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            otpUser = UserOtp(email = email, code = response.body()?.requestAt ?: "") // bingung
            true
        } else {
            false
        }
    }

    override suspend fun doVerifResendOtp(email: String): Boolean {
        val verifAccountOtpResendRequest = VerifAccountOtpResendRequest(email)
        val response = apiService.verifAccountOtpResend(verifAccountOtpResendRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            otpUserResend = UserOtpResend(email = response.body()?.requestAt ?: "") // bingung
            true
        } else {
            false
        }
    }

    // reset password
    override suspend fun reqChangePasswordByEmail(email: String): Boolean {
        val resetPasswordRequest =
            com.nafi.airseat.data.source.network.resetpassword.ResetPasswordRequest(email)
        val response = apiService.resetPassword(resetPasswordRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            otpUserResend = UserOtpResend(email = response.body()?.requestAt ?: "") // bingung
            true
        } else {
            false
        }
    }

    override suspend fun reqChangePasswordByEmailResendOtp(email: String): Boolean {
        val resetPasswordResendOtpRequest =
            com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResendOtpRequest(email)
        val response = apiService.resetPasswordResendOtp(resetPasswordResendOtpRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            otpUserResend = UserOtpResend(email = response.body()?.requestAt ?: "") // bingung
            true
        } else {
            false
        }
    }

    override suspend fun verifChangePasswordOtp(
        code: String,
        email: String,
        password: String,
        confirmPassword: String,
    ): Boolean {
        val verifChangePasswordOtpRequest =
            com.nafi.airseat.data.source.network.resetpassword.VerifyPasswordChangeOtpRequest(
                code,
                email,
                password,
                confirmPassword
            )
        val response = apiService.verifyPasswordChangeOtp(verifChangePasswordOtpRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            userChangePassword = UserChangePassword(code, email, password, confirmPassword = response.body()?.requestAt ?: "") // bingung
            true
        } else {
            false
        }
    }

    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    override fun getCurrentUser(): User? {
        return currentUser
    }

    override fun doLogout(): Boolean {
        return currentUser != null
    }
}
