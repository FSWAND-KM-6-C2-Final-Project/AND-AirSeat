package com.nafi.airseat.data.repository

import app.cash.turbine.test
import com.nafi.airseat.data.datasource.ProfileDataSource
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.profile.ProfileData
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun getDataProfile() {
        val mockResponse = mockk<BaseResponse<ProfileData>>(relaxed = true)
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
    fun updateProfileData() {
        val mockResponse = mockk<BaseResponse<Any>>(relaxed = true)
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
    fun deleteAccount() {
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
}
