package com.nafi.airseat.data.repository

import app.cash.turbine.test
import com.nafi.airseat.data.datasource.ProfileDataSource
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.profile.ProfileData
import com.nafi.airseat.data.source.network.model.profile.ProfileUser
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProfileRepositoryImplTest {
    @MockK
    lateinit var dataSource: ProfileDataSource

    private lateinit var repository: ProfileRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = ProfileRepositoryImpl(dataSource)
    }

    @Test
    fun `get dataProfile loading`() {
        val mockResponse = mockk<BaseResponse<ProfileData>>()
        runTest {
            coEvery { dataSource.getUserProfile() } returns mockResponse
            repository.getDataProfile().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getUserProfile() }
            }
        }
    }

    @Test
    fun `get dataProfile success`() {
        val dataProfile =
            ProfileUser(
                id = 123,
                fullName = "dakjhwdg",
                email = "adjhgw",
                phoneNumber = "9012836712",
                userStatus = "awdjkhaghw",
            )
        val mockData = ProfileData(dataProfile)
        val mockResponse = mockk<BaseResponse<ProfileData>>()
        every { mockResponse.data } returns mockData
        runTest {
            coEvery { dataSource.getUserProfile() } returns mockResponse
            repository.getDataProfile().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.getUserProfile() }
            }
        }
    }

    @Test
    fun `get dataProfile error`() {
        runTest {
            coEvery { dataSource.getUserProfile() } throws IllegalStateException("Error")
            repository.getDataProfile().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getUserProfile() }
            }
        }
    }

    @Test
    fun `update profileData Loading`() {
        val mockResponse = mockk<BaseResponse<Any>>()
        val mockBody = mockk<UpdateProfileRequest>(relaxed = true)
        runTest {
            coEvery { dataSource.updateUserProfile(any()) } returns mockResponse
            repository.updateProfileData(mockBody).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.updateUserProfile(any()) }
            }
        }
    }

    @Test
    fun `update profileData success`() {
        val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
        val mockBody = mockk<UpdateProfileRequest>(relaxed = true)
        runTest {
            coEvery { dataSource.updateUserProfile(any()) } returns mockResponse
            repository.updateProfileData(mockBody).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.updateUserProfile(any()) }
            }
        }
    }

    @Test
    fun `update profileData error`() {
        val mockBody = mockk<UpdateProfileRequest>()
        runTest {
            coEvery { dataSource.updateUserProfile(any()) } throws IllegalStateException("Error")
            repository.updateProfileData(mockBody).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.updateUserProfile(any()) }
            }
        }
    }

    @Test
    fun `delete account loading`() {
        val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
        runTest {
            coEvery { dataSource.deleteAccount() } returns mockResponse
            repository.deleteAccount().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.deleteAccount() }
            }
        }
    }

    @Test
    fun `delete account success`() {
        val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
        runTest {
            coEvery { dataSource.deleteAccount() } returns mockResponse
            repository.deleteAccount().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.deleteAccount() }
            }
        }
    }

    @Test
    fun `delete account error`() {
        runTest {
            coEvery { dataSource.deleteAccount() } throws IllegalStateException("Error")
            repository.deleteAccount().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.deleteAccount() }
            }
        }
    }
}
