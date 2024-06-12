package com.nafi.airseat.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafi.airseat.data.datasource.seatclass.SeatClassDummyDataSource
import com.nafi.airseat.data.model.SeatClass

interface SeatClassRepository {
    fun getSeatClasses(): LiveData<List<SeatClass>>
}

class SeatClassRepositoryImpl(private val dataSource: SeatClassDummyDataSource) : SeatClassRepository {
    override fun getSeatClasses(): LiveData<List<SeatClass>> {
        val data = MutableLiveData<List<SeatClass>>()
        data.value = dataSource.getSeatClasses()
        return data
    }
}
