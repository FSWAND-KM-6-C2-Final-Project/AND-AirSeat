package com.nafi.airseat.data.datasource.booking

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.booking.BookingData
import com.nafi.airseat.data.source.network.model.booking.BookingFlightRequest
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

class BookingApiDataSource(private val service: AirSeatApiServiceWithAuthorization) : BookingDataSource {
    override suspend fun createBooking(bookingFlightRequest: BookingFlightRequest): BaseResponse<BookingData> {
        return service.bookingFlight(bookingFlightRequest)
    }
}
