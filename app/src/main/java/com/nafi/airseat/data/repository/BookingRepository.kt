package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.booking.BookingDataSource
import com.nafi.airseat.data.model.Passenger
import com.nafi.airseat.data.source.network.model.booking.BookingFlightResponse
import com.nafi.airseat.data.source.network.model.booking.OrderedBy

interface BookingRepository {
    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun doBooking(
        flightId: Int,
        paymentMethod: String,
        discountId: Int,
        orderedBy: OrderedBy,
        passenger: List<Passenger>,
    ): BookingFlightResponse
}

class BookingRepositoryImpl(private val dataSource: BookingDataSource) : BookingRepository {
    override suspend fun doBooking(
        flightId: Int,
        paymentMethod: String,
        discountId: Int,
        orderedBy: OrderedBy,
        passenger: List<Passenger>,
    ): BookingFlightResponse {
        return dataSource.doBooking(flightId, paymentMethod, discountId, orderedBy, passenger)
    }
}
