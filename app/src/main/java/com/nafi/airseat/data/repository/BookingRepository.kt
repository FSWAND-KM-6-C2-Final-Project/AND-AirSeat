package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.booking.BookingDataSource
import com.nafi.airseat.data.source.network.model.booking.BookingFlightRequest
import com.nafi.airseat.data.source.network.model.booking.BookingPassenger
import com.nafi.airseat.data.source.network.model.booking.OrderedBy
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
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
            val bookingRespone =
                dataSource.createBooking(
                    BookingFlightRequest(flightId, returnFlightId, paymentMethod, orderedBy, passenger),
                )
            val redirectUrl = bookingRespone.data?.paymentData?.redirectUrl.orEmpty()
            redirectUrl
        }
    }
}
