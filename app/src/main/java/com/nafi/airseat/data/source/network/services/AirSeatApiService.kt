package com.nafi.airseat.data.source.network.services

import com.nafi.airseat.BuildConfig
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.repository.UserPrefRepository
import com.nafi.airseat.data.source.network.model.booking.BookingFlightRequest
import com.nafi.airseat.data.source.network.model.booking.BookingFlightResponse
import com.nafi.airseat.data.source.network.model.airport.AirportResponse
import com.nafi.airseat.data.source.network.model.flight.FlightsResponse
import com.nafi.airseat.data.source.network.model.flightdetail.FlightDetailResponse
import com.nafi.airseat.data.source.network.model.history.HistoryData
import com.nafi.airseat.data.source.network.model.login.LoginRequest
import com.nafi.airseat.data.source.network.model.notification.NotificationResponse
import com.nafi.airseat.data.source.network.model.profile.ProfileResponse
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileResponse
import com.nafi.airseat.data.source.network.model.register.RegisterData
import com.nafi.airseat.data.source.network.model.register.RegisterRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpResponse
import com.nafi.airseat.data.source.network.model.seat.SeatResponse
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpResendRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpResendResponse
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifAccountOtpResponse
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendData
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AirSeatApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): BaseResponse<Any>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest,
    ): BaseResponse<RegisterData>

    @POST("auth/activation/verify")
    suspend fun verifyAccountOtp(
        @Body verifyAccountOtpRequest: VerifyAccountOtpRequest,
    ): BaseResponse<Any>

    @POST("auth/activation/resend")
    suspend fun verifyAccountOtpResend(
        @Body verifyAccountOtpRequest: VerifyAccountOtpResendRequest,
    ): BaseResponse<VerifyAccountOtpResendData>

    @POST("auth/password-reset/resend")
    suspend fun resetPasswordResendOtp(
        @Body resetPasswordResendOtpRequest: ResetPasswordResendOtpRequest,
    ): BaseResponse<ResetPasswordResendOtpData>

    @POST("auth/password-reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest,
    ): BaseResponse<ResetPasswordData>

    @POST("auth/password-reset/verify")
    suspend fun verifyPasswordChangeOtp(
        @Body verifyPasswordChangeOtpRequest: VerifyPasswordChangeOtpRequest,
    ): BaseResponse<Any>

    @GET("airport")
    suspend fun getAirports(
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1,
    ): AirportResponse

    @GET("flight")
    suspend fun getFlights(
        @Query("searchDate") searchDate: String?,
        @Query("sortBy") sortBy: String?,
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1,
        @Query("order") order: String?,
        @Query("deptAirport") deptAirport: String?,
        @Query("arrAirport") arrAirport: String?,
    ): FlightsResponse

    @GET("flight/{id}")
    suspend fun getFlightsDetail(
        @Path("id") id: String,
    ): FlightDetailResponse

    @POST("api/v1/booking")
    suspend fun bookingFlight(
        @Body bookingFlightRequest: BookingFlightRequest,
    ): BookingFlightResponse

    @GET("seat/flight/1")
    suspend fun getSeatData(): SeatResponse

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

    @GET("booking/detail")
    suspend fun getHistoryData(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("bookingCode") bookingCode: String? = null,
    ): BaseResponse<HistoryData>

    @GET("auth/me")
    suspend fun getUserProfile(): ProfileResponse

    @PATCH("profile")
    suspend fun updateProfile(
        @Body updateProfileData: UpdateProfileRequest,
    ): UpdateProfileResponse

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
