package com.nafi.airseat.data

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

    suspend fun reqChangePasswordByEmail(email: String): Boolean

    fun isLoggedIn(): Boolean

    fun getCurrentUser(): User?

    fun doLogout(): Boolean
}

class AuthServiceImpl(private val apiService: AirSeatApiService) : AuthService {
    private var currentUser: User? = null
    private var otpUser: UserOtp? = null
    private var otpUserResend: UserOtpResend? = null

    override suspend fun doLogin(
        email: String,
        password: String,
    ): Boolean {
        val loginRequest = LoginRequest(email, password)
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
        val registerRequest = RegisterRequest(fullName, email, phoneNumber, password, confirmPassword)
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

    override suspend fun reqChangePasswordByEmail(email: String): Boolean {
        val resetPasswordRequest = ResetPasswordRequest(email)
        val response = apiService.resetPassword(resetPasswordRequest)
        return if (response.isSuccessful && response.body()?.status == true) {
            otpUserResend = UserOtpResend(email = response.body()?.requestAt ?: "") // bingung
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
