package com.nafi.airseat.data.source.network.services

import com.nafi.airseat.data.repository.UserPrefRepository
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val userPrefRepository: UserPrefRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = userPrefRepository.getToken()

        val authenticatedRequest =
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            // Token is invalid, refresh it
            synchronized(this) {
                userPrefRepository.clearToken()
            }
        }
        return response
    }
}
