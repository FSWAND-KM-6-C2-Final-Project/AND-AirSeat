package com.c2.airseat.data.datasource

import com.c2.airseat.data.model.BaseResponse
import com.c2.airseat.data.source.network.model.booking.BookingData
import com.c2.airseat.data.source.network.model.booking.BookingFlightRequest
import com.c2.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface BookingDataSource {
    suspend fun createBooking(bookingFlightRequest: BookingFlightRequest): BaseResponse<BookingData>
}

class BookingApiDataSource(private val service: AirSeatApiServiceWithAuthorization) :
    BookingDataSource {
    override suspend fun createBooking(bookingFlightRequest: BookingFlightRequest): BaseResponse<BookingData> {
        return service.bookingFlight(bookingFlightRequest)
    }
}
