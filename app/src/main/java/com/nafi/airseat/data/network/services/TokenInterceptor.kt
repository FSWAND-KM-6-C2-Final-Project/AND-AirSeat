package com.nafi.airseat.data.network.services

import com.nafi.airseat.data.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenRepository: TokenRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenRepository.getToken()

        val authenticatedRequest =
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            // Token is invalid, refresh it
            synchronized(this) {
                val newToken = tokenRepository.refreshToken()
                if (newToken != null) {
                    // Retry the original request with the new token
                    val newRequest =
                        originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newToken")
                            .build()
                    return chain.proceed(newRequest)
                }
            }
        }

        return response
    }
}
