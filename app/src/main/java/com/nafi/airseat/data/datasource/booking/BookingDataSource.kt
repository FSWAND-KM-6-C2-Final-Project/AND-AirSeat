package com.nafi.airseat.data.datasource.booking

import com.nafi.airseat.data.model.BaseResponse
import com.nafi.airseat.data.source.network.model.booking.BookingData
import com.nafi.airseat.data.source.network.model.booking.BookingFlightRequest

interface BookingDataSource {
    suspend fun createBooking(bookingFlightRequest: BookingFlightRequest): BaseResponse<BookingData>
}
