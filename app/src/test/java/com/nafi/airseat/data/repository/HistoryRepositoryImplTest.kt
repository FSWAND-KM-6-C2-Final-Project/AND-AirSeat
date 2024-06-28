package com.nafi.airseat.data.repository

import app.cash.turbine.test
import com.nafi.airseat.data.datasource.HistoryDataSource
import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.history.Airline
import com.nafi.airseat.data.source.network.model.history.ArrivalAirport
import com.nafi.airseat.data.source.network.model.history.Booking
import com.nafi.airseat.data.source.network.model.history.BookingDetail
import com.nafi.airseat.data.source.network.model.history.DepartureAirport
import com.nafi.airseat.data.source.network.model.history.Flight
import com.nafi.airseat.data.source.network.model.history.HistoryData
import com.nafi.airseat.data.source.network.model.history.Passenger
import com.nafi.airseat.data.source.network.model.history.ReturnArrivalAirport
import com.nafi.airseat.data.source.network.model.history.ReturnDepartureAirport
import com.nafi.airseat.data.source.network.model.history.ReturnFlight
import com.nafi.airseat.data.source.network.model.history.Seat
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

class HistoryRepositoryImplTest {
    @MockK
    lateinit var dataSource: HistoryDataSource

    private lateinit var repository: HistoryRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = HistoryRepositoryImpl(dataSource)
    }

    @Test
    fun `get historyData loading`() {
        val mockResponse = mockk<BaseResponse<HistoryData>>()
        runTest {
            coEvery { dataSource.getHistoryData(any(), any(), any()) } returns mockResponse
            repository.getHistoryData(
                bookingCode = "aksdhj",
                startDate = "dajhwg",
                endDate = "dajwghawd",
            ).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getHistoryData(any(), any(), any()) }
            }
        }
    }

    @Test
    fun `get historyData success`() {
        val mockHistories =
            listOf(
                Booking(
                    bookingCode = "ABC123",
                    bookingStatus = "Confirmed",
                    classes = "Economy",
                    createdAt = "2023-06-01T10:00:00Z",
                    duration = "5h 30m",
                    flight =
                        Flight(
                            arrivalAirport =
                                ArrivalAirport(
                                    airportCity = "New York",
                                    airportCityCode = "NYC",
                                    airportContinent = "North America",
                                    airportName = "John F. Kennedy International Airport",
                                    airportPicture = "https://example.com/jfk.jpg",
                                ),
                            arrivalTime = "2023-06-01T15:30:00Z",
                            departureAirport =
                                DepartureAirport(
                                    airportCity = "Los Angeles",
                                    airportCityCode = "LAX",
                                    airportContinent = "North America",
                                    airportName = "Los Angeles International Airport",
                                    airportPicture = "https://example.com/lax.jpg",
                                ),
                            departureTerminal = "Terminal 2",
                            departureTime = "2023-06-01T10:00:00Z",
                            flightNumber = "LA1234",
                            information = "On time",
                            airline =
                                Airline(
                                    airlineName = "Delta Airlines",
                                    airlinePicture = "https://example.com/delta.jpg",
                                ),
                        ),
                    returnFlight =
                        ReturnFlight(
                            arrivalAirport =
                                ReturnArrivalAirport(
                                    airportCity = "Los Angeles",
                                    airportCityCode = "LAX",
                                    airportContinent = "North America",
                                    airportName = "Los Angeles International Airport",
                                    airportPicture = "https://example.com/lax.jpg",
                                ),
                            arrivalTime = "2023-06-10T20:00:00Z",
                            departureAirport =
                                ReturnDepartureAirport(
                                    airportCity = "New York",
                                    airportCityCode = "NYC",
                                    airportContinent = "North America",
                                    airportName = "John F. Kennedy International Airport",
                                    airportPicture = "https://example.com/jfk.jpg",
                                ),
                            departureTerminal = "Terminal 3",
                            departureTime = "2023-06-10T15:00:00Z",
                            flightNumber = "NY5678",
                            information = "Delayed",
                            airline =
                                Airline(
                                    airlineName = "United Airlines",
                                    airlinePicture = "https://example.com/united.jpg",
                                ),
                        ),
                    totalAmount = 500000L,
                    bookingDetail =
                        listOf(
                            BookingDetail(
                                price = 250000,
                                seat =
                                    Seat(
                                        classX = "Economy",
                                        seatName = "12A",
                                    ),
                                passenger =
                                    Passenger(
                                        createdAt = "2023-06-01T09:00:00Z",
                                        dob = "1990-01-01",
                                        firstName = "John",
                                        id = 1,
                                        identificationCountry = "USA",
                                        identificationExpired = "2030-01-01",
                                        identificationNumber = "A1234567",
                                        identificationType = "Passport",
                                        passengerType = "Adult",
                                        lastName = "Doe",
                                        nationality = "American",
                                        title = "Mr.",
                                        updatedAt = "2023-06-01T09:00:00Z",
                                    ),
                            ),
                            BookingDetail(
                                price = 250000,
                                seat =
                                    Seat(
                                        classX = "Economy",
                                        seatName = "12B",
                                    ),
                                passenger =
                                    Passenger(
                                        createdAt = "2023-06-01T09:00:00Z",
                                        dob = "1992-02-02",
                                        firstName = "Jane",
                                        id = 2,
                                        identificationCountry = "USA",
                                        identificationExpired = "2030-01-01",
                                        identificationNumber = "B1234567",
                                        identificationType = "Passport",
                                        passengerType = "Adult",
                                        lastName = "Doe",
                                        nationality = "American",
                                        title = "Ms.",
                                        updatedAt = "2023-06-01T09:00:00Z",
                                    ),
                            ),
                        ),
                    paymentUrl = "https://payment.example.com",
                    bookingExpired = "awkjdhawk",
                    orderedByFirstName = "dawjhdaw",
                    orderedByPhoneNumber = "23131231451",
                    orderedByLastName = "8273618923",
                    paymentMethod = "snap",
                ),
            )
        val mockData = HistoryData(mockHistories)
        val mockResponse = mockk<BaseResponse<HistoryData>>(relaxed = true)
        every { mockResponse.data } returns mockData
        runTest {
            coEvery { dataSource.getHistoryData(any(), any(), any()) } returns mockResponse
            repository.getHistoryData(
                bookingCode = "aksdhj",
                startDate = "dajhwg",
                endDate = "dajwghawd",
            ).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.getHistoryData(any(), any(), any()) }
            }
        }
    }

    @Test
    fun `get historyData error`() {
        runTest {
            coEvery { dataSource.getHistoryData(any(), any(), any()) } throws
                IllegalStateException(
                    "Error",
                )
            repository.getHistoryData(
                bookingCode = "aksdhj",
                startDate = "dajhwg",
                endDate = "dajwghawd",
            ).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getHistoryData(any(), any(), any()) }
            }
        }
    }

    @Test
    fun `get historyData empty`() {
        val mockList = listOf<Booking>()
        val mockData = HistoryData(mockList)
        val mockResponse = mockk<BaseResponse<HistoryData>>()
        every { mockResponse.data } returns mockData
        runTest {
            coEvery { dataSource.getHistoryData(any(), any(), any()) } returns mockResponse
            repository.getHistoryData(
                bookingCode = "aksdhj",
                startDate = "dajhwg",
                endDate = "dajwghawd",
            ).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getHistoryData(any(), any(), any()) }
            }
        }
    }
}
