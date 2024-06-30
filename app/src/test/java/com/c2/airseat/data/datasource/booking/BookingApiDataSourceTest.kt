package com.c2.airseat.data.datasource.booking

import com.c2.airseat.data.datasource.BookingApiDataSource
import com.c2.airseat.data.datasource.BookingDataSource
import com.c2.airseat.data.model.BaseResponse
import com.c2.airseat.data.source.network.model.booking.BookingData
import com.c2.airseat.data.source.network.model.booking.BookingFlightRequest
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

class BookingApiDataSourceTest {
    @MockK
    lateinit var service: AirSeatApiServiceWithAuthorization

    private lateinit var dataSource: BookingDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = BookingApiDataSource(service)
    }

    @Test
    fun createBooking() {
        runTest {
            val mockResponse = mockk<BaseResponse<BookingData>>(relaxed = true)
            val mockBody = mockk<BookingFlightRequest>(relaxed = true)
            coEvery { service.bookingFlight(mockBody) } returns mockResponse
            val actualResult = dataSource.createBooking(mockBody)
            coVerify { service.bookingFlight(mockBody) }
            assertEquals(mockResponse, actualResult)
            assertEquals(mockResponse.status, actualResult.status)
        }
    }
}
