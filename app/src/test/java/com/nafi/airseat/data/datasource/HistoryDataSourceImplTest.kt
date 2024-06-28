package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.history.HistoryData
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

class HistoryDataSourceImplTest {
    @MockK
    lateinit var service: AirSeatApiServiceWithAuthorization

    private lateinit var dataSource: HistoryDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = HistoryDataSourceImpl(service)
    }

    @Test
    fun getHistoryData() {
        runTest {
            val mockResponse = mockk<BaseResponse<HistoryData>>(relaxed = true)
            coEvery {
                service.getHistoryData(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                )
            } returns mockResponse
            val actualResult =
                dataSource.getHistoryData(
                    bookingCode = "asdjhkga",
                    startDate = "dajkwygd",
                    endDate = "dajhwgda",
                )
            coVerify { service.getHistoryData(any(), any(), any(), any(), any(), any(), any()) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }
}
