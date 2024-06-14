package com.nafi.airseat.data.datasource.seatclass

import com.nafi.airseat.data.model.SeatClass

interface SeatClassDataSource {
    fun getSeatClasses(): List<SeatClass>
}
