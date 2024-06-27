package com.nafi.airseat.data.datasource.airport

import com.nafi.airseat.data.source.network.model.airport.AirportResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AirportApiDataSourceTest {
    @MockK
    lateinit var service: AirSeatApiService

    private lateinit var ds: AirportDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ds = AirportApiDataSource(service)
    }

    @Test
    fun getAirportByQuery() {
        runTest {
            val mockResponse = mockk<AirportResponse>(relaxed = true)
            coEvery { service.getAirportsByQuery(any()) } returns mockResponse
            val actualResponse = ds.getAirportByQuery("Jakarta")
            coVerify { service.getAirportsByQuery(any()) }
            assertEquals(actualResponse, mockResponse)
        }
    }
}
