package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.login.LoginRequest
import com.nafi.airseat.data.source.network.model.register.RegisterData
import com.nafi.airseat.data.source.network.model.register.RegisterRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordRequest
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpRequest
import com.nafi.airseat.data.source.network.model.resetpassword.VerifyPasswordChangeOtpRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpRequest
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendData
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendRequest
import com.nafi.airseat.data.source.network.services.AirSeatApiService

interface AuthDataSource {
    suspend fun doLogin(loginRequest: LoginRequest): BaseResponse<Any>

    suspend fun doRegister(registerRequest: RegisterRequest): BaseResponse<RegisterData>

    suspend fun doVerify(verifyAccountOtpRequest: VerifyAccountOtpRequest): BaseResponse<Any>

    suspend fun doVerifyResendOtp(verifyAccountOtpRequest: VerifyAccountOtpResendRequest): BaseResponse<VerifyAccountOtpResendData>

    suspend fun reqChangePasswordByEmail(resetPasswordRequest: ResetPasswordRequest): BaseResponse<ResetPasswordData>

    suspend fun reqChangePasswordByEmailResendOtp(
        resetPasswordResendOtpRequest: ResetPasswordResendOtpRequest,
    ): BaseResponse<ResetPasswordResendOtpData>

    suspend fun verifyChangePasswordOtp(verifyPasswordChangeOtpRequest: VerifyPasswordChangeOtpRequest): BaseResponse<Any>
}

class AuthDataSourceImpl(private val apiService: AirSeatApiService) : AuthDataSource {
    override suspend fun doLogin(loginRequest: LoginRequest): BaseResponse<Any> {
        return apiService.login(loginRequest)
    }

    override suspend fun doRegister(registerRequest: RegisterRequest): BaseResponse<RegisterData> {
        return apiService.register(registerRequest)
    }

    override suspend fun doVerify(verifyAccountOtpRequest: VerifyAccountOtpRequest): BaseResponse<Any> {
        return apiService.verifyAccountOtp(verifyAccountOtpRequest)
    }

    override suspend fun doVerifyResendOtp(
        verifyAccountOtpRequest: VerifyAccountOtpResendRequest,
    ): BaseResponse<VerifyAccountOtpResendData> {
        return apiService.verifyAccountOtpResend(verifyAccountOtpRequest)
    }

    override suspend fun reqChangePasswordByEmail(resetPasswordRequest: ResetPasswordRequest): BaseResponse<ResetPasswordData> {
        return apiService.resetPassword(resetPasswordRequest)
    }

    override suspend fun reqChangePasswordByEmailResendOtp(
        resetPasswordResendOtpRequest: ResetPasswordResendOtpRequest,
    ): BaseResponse<ResetPasswordResendOtpData> {
        return apiService.resetPasswordResendOtp(resetPasswordResendOtpRequest)
    }

    override suspend fun verifyChangePasswordOtp(verifyPasswordChangeOtpRequest: VerifyPasswordChangeOtpRequest): BaseResponse<Any> {
        return apiService.verifyPasswordChangeOtp(verifyPasswordChangeOtpRequest)
    }
}
