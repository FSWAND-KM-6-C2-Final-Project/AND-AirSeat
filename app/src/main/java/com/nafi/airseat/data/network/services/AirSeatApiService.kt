package com.nafi.airseat.data.network.services

import com.nafi.airseat.BuildConfig
import com.nafi.airseat.data.network.login.LoginRequest
import com.nafi.airseat.data.network.login.LoginResponse
import com.nafi.airseat.data.network.register.RegisterRequest
import com.nafi.airseat.data.network.register.RegisterResponse
import com.nafi.airseat.data.network.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.network.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.network.resetpassword.ResetPasswordResendOtpResponse
import com.nafi.airseat.data.network.resetpassword.ResetPasswordResponse
import com.nafi.airseat.data.network.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.network.resetpassword.VerifyPasswordChangeOtpResponse
import com.nafi.airseat.data.network.seat.SeatResponse
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpRequest
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResendRequest
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResendResponse
import com.nafi.airseat.data.network.verifyaccount.VerifAccountOtpResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET("api/v1/seat/flight/1")
    suspend fun getSeats(): SeatResponse

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
