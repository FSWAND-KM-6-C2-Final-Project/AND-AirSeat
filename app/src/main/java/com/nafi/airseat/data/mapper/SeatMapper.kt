package com.nafi.airseat.data.mapper

import com.nafi.airseat.data.model.Seat

fun com.nafi.airseat.data.source.network.model.seat.Seat?.toSeat() =
    Seat(
        seatRow = this?.seatRow.orEmpty(),
        seatName = this?.seatName.orEmpty(),
        flightId = this?.flightId ?: 0,
        seatColumn = this?.seatColumn ?: 1,
        seatStatusAndroid = this?.seatStatusAndroid.orEmpty(),
    )

fun Collection<com.nafi.airseat.data.source.network.model.seat.Seat>.toSeats() =
    this.map {
        it.toSeat()
    }
