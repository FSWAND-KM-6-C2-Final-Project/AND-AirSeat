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
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthBookingDataSourceTest {
    @MockK
    lateinit var service: AirSeatApiService

    private lateinit var dataSource: AuthDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = AuthDataSourceImpl(service)
    }

    @Test
    fun doLogin() {
        runTest {
            val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
            val mockBody = mockk<LoginRequest>(relaxed = true)
            coEvery { service.login(mockBody) } returns mockResponse
            val actualResult = dataSource.doLogin(mockBody)
            coVerify { service.login(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun doRegister() {
        runTest {
            val mockResponse = mockk<BaseResponse<RegisterData>>(relaxed = true)
            val mockBody = mockk<RegisterRequest>(relaxed = true)
            coEvery { service.register(mockBody) } returns mockResponse
            val actualResult = dataSource.doRegister(mockBody)
            coVerify { service.register(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun doVerify() {
        runTest {
            val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
            val mockBody = mockk<VerifyAccountOtpRequest>(relaxed = true)
            coEvery { service.verifyAccountOtp(mockBody) } returns mockResponse
            val actualResult = dataSource.doVerify(mockBody)
            coVerify { service.verifyAccountOtp(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun doVerifyResendOtp() {
        runTest {
            val mockResponse = mockk<BaseResponse<VerifyAccountOtpResendData>>(relaxed = true)
            val mockBody = mockk<VerifyAccountOtpResendRequest>(relaxed = true)
            coEvery { service.verifyAccountOtpResend(mockBody) } returns mockResponse
            val actualResult = dataSource.doVerifyResendOtp(mockBody)
            coVerify { service.verifyAccountOtpResend(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun reqChangePasswordByEmail() {
        runTest {
            val mockResponse = mockk<BaseResponse<ResetPasswordData>>(relaxed = true)
            val mockBody = mockk<ResetPasswordRequest>(relaxed = true)
            coEvery { service.resetPassword(mockBody) } returns mockResponse
            val actualResult = dataSource.reqChangePasswordByEmail(mockBody)
            coVerify { service.resetPassword(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun reqChangePasswordByEmailResendOtp() {
        runTest {
            val mockResponse = mockk<BaseResponse<ResetPasswordResendOtpData>>(relaxed = true)
            val mockBody = mockk<ResetPasswordResendOtpRequest>(relaxed = true)
            coEvery { service.resetPasswordResendOtp(mockBody) } returns mockResponse
            val actualResult = dataSource.reqChangePasswordByEmailResendOtp(mockBody)
            coVerify { service.resetPasswordResendOtp(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun verifyChangePasswordOtp() {
        runTest {
            val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
            val mockBody = mockk<VerifyPasswordChangeOtpRequest>(relaxed = true)
            coEvery { service.verifyPasswordChangeOtp(mockBody) } returns mockResponse
            val actualResult = dataSource.verifyChangePasswordOtp(mockBody)
            coVerify { service.verifyPasswordChangeOtp(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }
}
