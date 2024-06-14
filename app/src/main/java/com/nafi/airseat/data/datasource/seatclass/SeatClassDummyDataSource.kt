package com.nafi.airseat.data.datasource.seatclass

import com.nafi.airseat.data.model.SeatClass

interface SeatClassDummyDataSource {
    fun getSeatClasses(): List<SeatClass>
}

class SeatClassDummyDataSourceImpl : SeatClassDummyDataSource {
    override fun getSeatClasses(): List<SeatClass> {
        return listOf(
            SeatClass(
                id = 1,
                seatName = "Economy",
                seatPrice = 2000000,
            ),
            SeatClass(
                id = 2,
                seatName = "Premium Economy",
                seatPrice = 3000000,
            ),
            SeatClass(
                id = 3,
                seatName = "Business",
                seatPrice = 5000000,
            ),
            SeatClass(
                id = 4,
                seatName = "First Class",
                seatPrice = 7000000,
            ),
        )
    }
}
