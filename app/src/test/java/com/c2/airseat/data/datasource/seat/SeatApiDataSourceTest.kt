package com.c2.airseat.data.datasource.seat

import com.c2.airseat.data.datasource.SeatApiDataSource
import com.c2.airseat.data.datasource.SeatDataSource
import com.c2.airseat.data.source.network.model.seat.SeatResponse
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

class SeatApiDataSourceTest {
    @MockK
    lateinit var service: AirSeatApiServiceWithAuthorization

    private lateinit var dataSource: SeatDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = SeatApiDataSource(service)
    }

    @Test
    fun getSeats() {
        runTest {
            val mockResponse = mockk<SeatResponse>(relaxed = true)
            coEvery { service.getSeatData(any(), any()) } returns mockResponse
            val actualResult = dataSource.getSeats("1", "Economy")
            coVerify { service.getSeatData(any(), any()) }
            assertEquals(mockResponse, actualResult)
        }
    }
}
