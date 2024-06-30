package com.c2.airseat.data.datasource

import com.c2.airseat.data.model.BaseResponse
import com.c2.airseat.data.source.network.model.profile.ProfileData
import com.c2.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.c2.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProfileDataSourceImplTest {
    @MockK
    lateinit var service: AirSeatApiServiceWithAuthorization

    private lateinit var dataSource: ProfileDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = ProfileDataSourceImpl(service)
    }

    @Test
    fun getUserProfile() {
        runTest {
            val mockResponse = mockk<BaseResponse<ProfileData>>(relaxed = true)
            coEvery { service.getUserProfile() } returns mockResponse
            val actualResult = dataSource.getUserProfile()
            coVerify { service.getUserProfile() }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun updateUserProfile() {
        runTest {
            val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
            val mockBody = mockk<UpdateProfileRequest>(relaxed = true)
            coEvery { service.updateProfile(mockBody) } returns mockResponse
            val actualResult = dataSource.updateUserProfile(mockBody)
            coVerify { service.updateProfile(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }

    @Test
    fun deleteAccount() {
        val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
        runTest {
            coEvery { service.deleteAccount() } returns mockResponse
            val actualResult = dataSource.deleteAccount()
            coVerify { service.deleteAccount() }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }
}
