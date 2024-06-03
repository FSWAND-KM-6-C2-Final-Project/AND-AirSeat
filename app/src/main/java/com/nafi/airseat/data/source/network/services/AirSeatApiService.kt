package com.nafi.airseat.data.source.network.services

import com.nafi.airseat.BuildConfig
import com.nafi.airseat.data.source.network.login.LoginRequest
import com.nafi.airseat.data.source.network.login.LoginResponse
import com.nafi.airseat.data.source.network.register.RegisterRequest
import com.nafi.airseat.data.source.network.register.RegisterResponse
import com.nafi.airseat.data.source.network.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResendOtpResponse
import com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResponse
import com.nafi.airseat.data.source.network.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.source.network.resetpassword.VerifyPasswordChangeOtpResponse
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpRequest
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResendRequest
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResendResponse
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AirSeatApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: com.nafi.airseat.data.source.network.login.LoginRequest,
    ): Response<com.nafi.airseat.data.source.network.login.LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: com.nafi.airseat.data.source.network.register.RegisterRequest,
    ): Response<com.nafi.airseat.data.source.network.register.RegisterResponse>

    @POST("auth/activation/verify")
    suspend fun verifAccountOtp(
        @Body verifAccountOtpRequest: VerifAccountOtpRequest,
    ): Response<VerifAccountOtpResponse>

    @POST("auth/activation/resend")
    suspend fun verifAccountOtpResend(
        @Body verifAccountOtpRequest: VerifAccountOtpResendRequest,
    ): Response<VerifAccountOtpResendResponse>

    // reset password
    @POST("auth/password-reset/resend")
    suspend fun resetPasswordResendOtp(
        @Body resetPasswordResendOtpRequest: com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResendOtpRequest,
    ): Response<com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResendOtpResponse>

    @POST("auth/password-reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: com.nafi.airseat.data.source.network.resetpassword.ResetPasswordRequest,
    ): Response<com.nafi.airseat.data.source.network.resetpassword.ResetPasswordResponse>

    @POST("auth/password-reset/verify")
    suspend fun verifyPasswordChangeOtp(
        @Body verifyPasswordChangeOtpRequest: com.nafi.airseat.data.source.network.resetpassword.VerifyPasswordChangeOtpRequest,
    ): Response<com.nafi.airseat.data.source.network.resetpassword.VerifyPasswordChangeOtpResponse>

    @POST("auth/me")
    fun refreshToken(): Call<com.nafi.airseat.data.source.network.services.AirSeatApiService.RefreshTokenResponse>

    companion object {
        @JvmStatic
        operator fun invoke(): com.nafi.airseat.data.source.network.services.AirSeatApiService {
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(com.nafi.airseat.data.source.network.services.AirSeatApiService::class.java)
        }
    }

    data class RefreshTokenResponse(val token: String)
}
