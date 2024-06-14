package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.ProfileDataSource
import com.nafi.airseat.data.mapper.toDataProfile
import com.nafi.airseat.data.model.DataProfile
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getDataProfile(): Flow<ResultWrapper<DataProfile>>
}

class ProfileRepositoryImpl(private val datasource: ProfileDataSource) : ProfileRepository {
    override fun getDataProfile(): Flow<ResultWrapper<DataProfile>> {
        return proceedFlow { datasource.getUserProfile().data.user.toDataProfile() }
    }
}
