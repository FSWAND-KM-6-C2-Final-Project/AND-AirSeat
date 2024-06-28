package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.notification.NotificationList
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NotificationDataSourceImplTest {
    @MockK
    lateinit var service: AirSeatApiServiceWithAuthorization

    private lateinit var dataSource: NotificationDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = NotificationDataSourceImpl(service)
    }

    @Test
    fun getNotification() {
        runTest {
            val mockResponse = mockk<BaseResponse<NotificationList>>(relaxed = true)
            coEvery { service.getNotification() } returns mockResponse
            val actualResult = dataSource.getNotification()
            coVerify { service.getNotification() }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }
}
