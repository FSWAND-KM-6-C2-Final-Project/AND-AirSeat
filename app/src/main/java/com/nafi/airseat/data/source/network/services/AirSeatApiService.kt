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
import com.nafi.airseat.data.source.network.verifyaccount.VerifAccountOtpRequest
import com.nafi.airseat.data.source.network.verifyaccount.VerifAccountOtpResendRequest
import com.nafi.airseat.data.source.network.verifyaccount.VerifAccountOtpResendResponse
import com.nafi.airseat.data.source.network.verifyaccount.VerifAccountOtpResponse
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
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): Response<RegisterResponse>

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
        @Body resetPasswordResendOtpRequest: ResetPasswordResendOtpRequest,
    ): Response<ResetPasswordResendOtpResponse>

    @POST("auth/password-reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest,
    ): Response<ResetPasswordResponse>

    @POST("auth/password-reset/verify")
    suspend fun verifyPasswordChangeOtp(
        @Body verifyPasswordChangeOtpRequest: VerifyPasswordChangeOtpRequest,
    ): Response<VerifyPasswordChangeOtpResponse>

    @POST("auth/me")
    fun refreshToken(): Call<RefreshTokenResponse>

    companion object {
        @JvmStatic
        operator fun invoke(): AirSeatApiService {
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
            return retrofit.create(AirSeatApiService::class.java)
        }
    }

    data class RefreshTokenResponse(val token: String)
}
