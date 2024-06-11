package com.nafi.airseat.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafi.airseat.data.datasource.favoritedestination.FavoriteDestinationDataSource
import com.nafi.airseat.data.model.FavoriteDestination

interface FavoriteDestinationRepository {
    fun getFavoriteDestinations(): LiveData<List<FavoriteDestination>>
}

class FavoriteDestinationRepositoryImpl(private val dataSource: FavoriteDestinationDataSource) : FavoriteDestinationRepository {
    override fun getFavoriteDestinations(): LiveData<List<FavoriteDestination>> {
        val data = MutableLiveData<List<FavoriteDestination>>()
        data.value = dataSource.getFavoriteDestinations()
        return data
    }
}
