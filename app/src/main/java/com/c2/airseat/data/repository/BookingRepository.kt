package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.BookingDataSource
import com.c2.airseat.data.source.network.model.booking.BookingFlightRequest
import com.c2.airseat.data.source.network.model.booking.BookingPassenger
import com.c2.airseat.data.source.network.model.booking.OrderedBy
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun doBooking(
        flightId: Int,
        returnFlightId: Int?,
        paymentMethod: String,
        orderedBy: OrderedBy,
        passenger: List<BookingPassenger>,
    ): Flow<ResultWrapper<String>>
}

class BookingRepositoryImpl(private val dataSource: BookingDataSource) : BookingRepository {
    override fun doBooking(
        flightId: Int,
        returnFlightId: Int?,
        paymentMethod: String,
        orderedBy: OrderedBy,
        passenger: List<BookingPassenger>,
    ): Flow<ResultWrapper<String>> {
        return proceedFlow {
            val bookingResponse =
                dataSource.createBooking(
                    BookingFlightRequest(flightId, returnFlightId, paymentMethod, orderedBy, passenger),
                )
            val redirectUrl = bookingResponse.data?.paymentData?.redirectUrl.orEmpty()
            redirectUrl
        }
    }
}
