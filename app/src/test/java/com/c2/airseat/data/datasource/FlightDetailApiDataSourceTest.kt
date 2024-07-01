package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.network.model.flightdetail.FlightDetailResponse
import com.c2.airseat.data.source.network.services.AirSeatApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FlightDetailApiDataSourceTest {
    @MockK
    lateinit var service: AirSeatApiService

    private lateinit var ds: FlightDetailDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ds = FlightDetailApiDataSource(service)
    }

    @Test
    fun getFlightDetailList() {
        runTest {
            val mockResponse = mockk<FlightDetailResponse>(relaxed = true)
            coEvery { service.getFlightsDetail(any()) } returns mockResponse
            val actualResponse = ds.getFlightDetailList("1")
            coVerify { service.getFlightsDetail(any()) }
            assertEquals(actualResponse, mockResponse)
        }
    }
}
