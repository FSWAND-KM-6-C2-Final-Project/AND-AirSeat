package com.nafi.airseat.data.source.network.services

import com.nafi.airseat.BuildConfig
import com.nafi.airseat.data.source.network.model.airport.AirportsResponse
import com.nafi.airseat.data.repository.UserPrefRepository
import com.nafi.airseat.data.source.network.model.airport.AirportResponse
import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.model.login.LoginRequest
import com.nafi.airseat.data.source.network.model.login.LoginResponse
import com.nafi.airseat.data.source.network.model.notification.NotificationResponse
import com.nafi.airseat.data.source.network.model.register.RegisterRequest
import com.nafi.airseat.data.source.network.model.register.RegisterResponse
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpResponse
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResponse
import com.nafi.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpResponse
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpResendRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpResendResponse
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AirSeatApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse

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

    @GET("auth/me")
    suspend fun checkUserInformation(
        @Header("Authorization") token: String? = null,
    ): LoginResponse

    @GET("auth/me")
    fun refreshToken(): Call<RefreshTokenResponse>

    @GET("airport")
    suspend fun getAirports(
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1,
    ): AirportResponse

    @GET("flight")
    suspend fun getFlights(
        @Query("searchDate") searchDate: String,
        @Query("sortBy") sortBy: String,
    ): FlightsResponse

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

interface AirSeatApiServiceWithAuthorization {
    @GET("notification")
    suspend fun getNotification(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
    ): NotificationResponse

    companion object {
        @JvmStatic
        operator fun invoke(userPrefRepository: UserPrefRepository): AirSeatApiServiceWithAuthorization {
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(TokenInterceptor(userPrefRepository))
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(AirSeatApiServiceWithAuthorization::class.java)
        }
    }
}
