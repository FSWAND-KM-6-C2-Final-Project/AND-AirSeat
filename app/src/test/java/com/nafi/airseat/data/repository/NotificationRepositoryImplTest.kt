package com.nafi.airseat.data.repository

import app.cash.turbine.test
import com.nafi.airseat.data.datasource.NotificationDataSource
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.notification.NotificationData
import com.nafi.airseat.data.source.network.model.notification.NotificationList
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationRepositoryImplTest {
    @MockK
    lateinit var dataSource: NotificationDataSource

    private lateinit var repository: NotificationRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = NotificationRepositoryImpl(dataSource)
    }

    @Test
    fun `get notification loading`() {
        val mockResponse = mockk<BaseResponse<NotificationList>>()
        runTest {
            coEvery { dataSource.getNotification() } returns mockResponse
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `get notification success`() {
        val notification1 =
            NotificationData(
                id = 12341,
                notificationType = "ajkugdajw",
                notificationTitle = "djahwdaw",
                notificationDescription = "dajhwdga",
                updatedAt = "dahwdga",
            )

        val notification2 =
            NotificationData(
                id = 12341,
                notificationType = "ajkugdajw",
                notificationTitle = "djahwdaw",
                notificationDescription = "dajhwdga",
                updatedAt = "dahwdga",
            )

        val mockList = listOf(notification1, notification2)
        val mockData = NotificationList(mockList)
        val mockResponse = mockk<BaseResponse<NotificationList>>()
        every { mockResponse.data } returns mockData
        runTest {
            coEvery { dataSource.getNotification() } returns mockResponse
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `get notification error`() {
        runTest {
            coEvery { dataSource.getNotification() } throws IllegalStateException("Error")
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getNotification() }
            }
        }
    }

    @Test
    fun `get notification empty`() {
        val mockList = listOf<NotificationData>()
        val mockData = NotificationList(mockList)
        val mockResponse = mockk<BaseResponse<NotificationList>>()
        every { mockResponse.data } returns mockData
        runTest {
            coEvery { dataSource.getNotification() } returns mockResponse
            repository.getNotification().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getNotification() }
            }
        }
    }
}
