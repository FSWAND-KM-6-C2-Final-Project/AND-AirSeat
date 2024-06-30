package com.c2.airseat.presentation.flightdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.c2.airseat.data.repository.BookingRepository
import com.c2.airseat.data.repository.FlightDetailRepository
import com.c2.airseat.data.source.network.model.booking.BookingPassenger
import com.c2.airseat.data.source.network.model.booking.OrderedBy
import kotlinx.coroutines.Dispatchers

class FlightDetailPriceViewModel(private val bookingRepository: BookingRepository, private val flightDetailRepository: FlightDetailRepository) : ViewModel() {
    fun doBooking(
        flightId: Int,
        returnFlightId: Int?,
        paymentMethod: String,
        orderedBy: OrderedBy,
        passenger: List<BookingPassenger>,
    ) = bookingRepository.doBooking(flightId, returnFlightId, paymentMethod, orderedBy, passenger).asLiveData(Dispatchers.IO)

    fun getDetailFlight(id: String) = flightDetailRepository.getFlightDetailList(id).asLiveData(Dispatchers.IO)
}
