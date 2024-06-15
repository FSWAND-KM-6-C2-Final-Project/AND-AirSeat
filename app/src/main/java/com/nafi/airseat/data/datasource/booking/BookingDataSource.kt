package com.nafi.airseat.data.datasource.booking

import com.nafi.airseat.data.model.Passenger
import com.nafi.airseat.data.source.network.model.booking.BookingFlightRequest
import com.nafi.airseat.data.source.network.model.booking.BookingFlightResponse
import com.nafi.airseat.data.source.network.model.booking.OrderedBy
import com.nafi.airseat.data.source.network.services.AirSeatApiService

interface BookingDataSource {
    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun doBooking(
        flightId: Int,
        paymentMethod: String,
        discountId: Int,
        orderedBy: OrderedBy,
        passenger: List<Passenger>,
    ): BookingFlightResponse
}

class BookingDataSourceImpl(private val apiService: AirSeatApiService) : BookingDataSource {
    override suspend fun doBooking(
        flightId: Int,
        paymentMethod: String,
        discountId: Int,
        orderedBy: OrderedBy,
        passenger: List<Passenger>,
    ): BookingFlightResponse {
        val bookingFlightRequest =
            BookingFlightRequest(
                flightId,
                paymentMethod,
                discountId,
                orderedBy,
                passenger,
            )
        return apiService.bookingFlight(bookingFlightRequest)
    }
}
