package com.c2.airseat.data.repository

import app.cash.turbine.test
import com.c2.airseat.data.datasource.BookingDataSource
import com.c2.airseat.data.model.BaseResponse
import com.c2.airseat.data.source.network.model.booking.BookingData
import com.c2.airseat.data.source.network.model.booking.BookingPassenger
import com.c2.airseat.data.source.network.model.booking.OrderedBy
import com.c2.airseat.data.source.network.model.booking.SeatPassenger
import com.c2.airseat.utils.ResultWrapper
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

class BookingRepositoryImplTest {
    @MockK
    lateinit var dataSource: BookingDataSource

    private lateinit var repository: BookingRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = BookingRepositoryImpl(dataSource)
    }

    @Test
    fun doBooking() {
        val flightId = 1
        val returnFlightId = 1
        val paymentMethod = "snap"
        val orderedBy = OrderedBy("Airseat", "", "081211518758", "airseat@gmail.com")
        val passenger =
            listOf(
                BookingPassenger(
                    "Air", "Seat", "2024-06-22T17:00:00.000Z", "mrs", "Indonesia", "paspor", "123210035", "Indonesia", "2024-10-22T17:00:00.000Z", "adult",
                    SeatPassenger("A", "1"), SeatPassenger("C", "5"),
                ),
            )
        val mockResponse = mockk<BaseResponse<BookingData>>()
        runTest {
            coEvery { dataSource.createBooking(any()) } returns mockResponse
            repository.doBooking(flightId, returnFlightId, paymentMethod, orderedBy, passenger).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.createBooking(any()) }
            }
        }
    }
}
