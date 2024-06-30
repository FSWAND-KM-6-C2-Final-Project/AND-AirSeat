package com.c2.airseat.data.mapper

import com.c2.airseat.data.model.Seat

fun com.c2.airseat.data.source.network.model.seat.Seat?.toSeat() =
    Seat(
        id = this?.id ?: 0,
        seatRow = this?.seatRow.orEmpty(),
        seatName = this?.seatName.orEmpty(),
        flightId = this?.flightId ?: 0,
        seatColumn = this?.seatColumn ?: 1,
        seatStatusAndroid = this?.seatStatusAndroid.orEmpty(),
    )

fun Collection<com.c2.airseat.data.source.network.model.seat.Seat>.toSeats() =
    this.map {
        it.toSeat()
    }
