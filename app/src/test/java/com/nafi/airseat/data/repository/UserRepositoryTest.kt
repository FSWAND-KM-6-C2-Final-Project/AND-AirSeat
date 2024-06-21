package com.nafi.airseat.data.repository

import app.cash.turbine.test
import com.nafi.airseat.data.datasource.AuthDataSource
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.register.RegisterData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordData
import com.nafi.airseat.data.source.network.model.resetpassword.ResetPasswordResendOtpData
import com.nafi.airseat.data.source.network.model.verifyaccount.VerifyAccountOtpResendData
import com.nafi.airseat.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    @MockK
    lateinit var dataSource: AuthDataSource

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun doLogin() {
        val email = "asnafialkaromi@gmail.com"
        val password = "qwerty123"
        val mockResponse = mockk<BaseResponse<Any>>()
        runTest {
            coEvery { dataSource.doLogin(any()) } returns mockResponse
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.doLogin(any()) }
            }
        }
    }

    @Test
    fun doRegister() {
        val fullName = "Asnafi"
        val email = "asnafialkaromi@gmail.com"
        val password = "qwerty123"
        val confPassword = "qwerty123"
        val phoneNumber = "081234567890"
        val mockK = mockk<BaseResponse<RegisterData>>(relaxed = true)
        runTest {
            coEvery { dataSource.doRegister(any()) } returns mockK
            repository.doRegister(fullName, phoneNumber, email, password, confPassword).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.doRegister(any()) }
            }
        }
    }

    @Test
    fun doVerify() {
        val email = "asnafialkaromi@gmail.com"
        val code = "123456"
        val mockResponse = mockk<BaseResponse<Any>>()
        runTest {
            coEvery { dataSource.doVerify(any()) } returns mockResponse
            repository.doVerify(email, code).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.doVerify(any()) }
            }
        }
    }

    @Test
    fun doVerifyResendOtp() {
        val email = "asnafialkaromi@gmail.com"
        val mockResponse = mockk<BaseResponse<VerifyAccountOtpResendData>>()
        runTest {
            coEvery { dataSource.doVerifyResendOtp(any()) } returns mockResponse
            repository.doVerifyResendOtp(email).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.doVerifyResendOtp(any()) }
            }
        }
    }

    @Test
    fun reqChangePasswordByEmail() {
        val email = "asnafialkaromi@gmail.com"
        val mockResponse = mockk<BaseResponse<ResetPasswordData>>()
        runTest {
            coEvery { dataSource.reqChangePasswordByEmail(any()) } returns mockResponse
            repository.reqChangePasswordByEmail(email).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.reqChangePasswordByEmail(any()) }
            }
        }
    }

    @Test
    fun reqChangePasswordByEmailResendOtp() {
        val email = "asnafialkaromi@gmail.com"
        val mockResponse = mockk<BaseResponse<ResetPasswordResendOtpData>>()
        runTest {
            coEvery { dataSource.reqChangePasswordByEmailResendOtp(any()) } returns mockResponse
            repository.reqChangePasswordByEmailResendOtp(email).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.reqChangePasswordByEmailResendOtp(any()) }
            }
        }
    }

    @Test
    fun verifyChangePasswordOtp() {
        val email = "asnafialkaromi@gmail.com"
        val password = "qwerty123"
        val confPassword = "qwerty123"
        val code = "123456"
        val mockResponse = mockk<BaseResponse<Any>>()
        runTest {
            coEvery { dataSource.verifyChangePasswordOtp(any()) } returns mockResponse
            repository.verifyChangePasswordOtp(code, confPassword, email, password).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                Assert.assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.verifyChangePasswordOtp(any()) }
            }
        }
    }
}
