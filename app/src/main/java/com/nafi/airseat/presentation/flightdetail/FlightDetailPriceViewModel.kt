package com.nafi.airseat.presentation.flightdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.nafi.airseat.data.model.Passenger
import com.nafi.airseat.data.repository.BookingRepository
import com.nafi.airseat.data.repository.FlightDetailRepository
import com.nafi.airseat.data.source.network.model.booking.BookingFlightResponse
import com.nafi.airseat.data.source.network.model.booking.OrderedBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlightDetailPriceViewModel(private val bookingRepository: BookingRepository, private val flightDetailRepository: FlightDetailRepository) : ViewModel() {
    suspend fun doBooking(
        flightId: Int,
        paymentMethod: String,
        discountId: Int,
        orderedBy: OrderedBy,
        passenger: List<Passenger>,
    ): BookingFlightResponse {
        return withContext(Dispatchers.IO) {
            bookingRepository.doBooking(flightId, paymentMethod, discountId, orderedBy, passenger)
        }
    }

    fun getDetailFlight(id: String) = flightDetailRepository.getFlightDetailList(id).asLiveData(Dispatchers.IO)
}
