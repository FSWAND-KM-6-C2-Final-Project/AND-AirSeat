package com.nafi.airseat.data

interface AuthService {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(
        email: String,
        password: String,
    ): Boolean

    fun isLoggedIn(): Boolean

    fun getCurrentUser(): User?
}

class AuthServiceImpl(private val apiService: AirSeatApiService) : AuthService {
    private var currentUser: User? = null

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

    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    override fun getCurrentUser(): User? {
        return currentUser
    }
}
