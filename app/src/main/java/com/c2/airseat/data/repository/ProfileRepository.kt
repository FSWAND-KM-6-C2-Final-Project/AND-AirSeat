package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.ProfileDataSource
import com.c2.airseat.data.mapper.toDataProfile
import com.c2.airseat.data.model.DataProfile
import com.c2.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.c2.airseat.utils.ResultWrapper
import com.c2.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getDataProfile(): Flow<ResultWrapper<DataProfile>>

    fun updateProfileData(updateProfileRequest: UpdateProfileRequest): Flow<ResultWrapper<String>>

    fun deleteAccount(): Flow<ResultWrapper<String>>
}

class ProfileRepositoryImpl(private val datasource: ProfileDataSource) : ProfileRepository {
    override fun getDataProfile(): Flow<ResultWrapper<DataProfile>> {
        return proceedFlow { datasource.getUserProfile().data?.user.toDataProfile() }
    }

    override fun updateProfileData(updateProfileRequest: UpdateProfileRequest): Flow<ResultWrapper<String>> {
        return proceedFlow { datasource.updateUserProfile(updateProfileRequest).message.orEmpty() }
    }

    override fun deleteAccount(): Flow<ResultWrapper<String>> {
        return proceedFlow { datasource.deleteAccount().message.orEmpty() }
    }
}
