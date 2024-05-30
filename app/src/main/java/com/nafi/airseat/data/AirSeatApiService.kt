package com.nafi.airseat.data

import com.nafi.airseat.BuildConfig
import okhttp3.OkHttpClient
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

    //reset password
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
}
