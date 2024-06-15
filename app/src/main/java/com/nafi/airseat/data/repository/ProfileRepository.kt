package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.ProfileDataSource
import com.nafi.airseat.data.mapper.toDataProfile
import com.nafi.airseat.data.mapper.toUpdateProfile
import com.nafi.airseat.data.model.DataProfile
import com.nafi.airseat.data.model.UpdateProfile
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.utils.ResultWrapper
import com.nafi.airseat.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getDataProfile(): Flow<ResultWrapper<DataProfile>>

    fun updateProfileData(updateProfileRequest: UpdateProfileRequest): Flow<ResultWrapper<UpdateProfile>>
}

class ProfileRepositoryImpl(private val datasource: ProfileDataSource) : ProfileRepository {
    override fun getDataProfile(): Flow<ResultWrapper<DataProfile>> {
        return proceedFlow { datasource.getUserProfile().data.user.toDataProfile() }
    }

    override fun updateProfileData(updateProfileRequest: UpdateProfileRequest): Flow<ResultWrapper<UpdateProfile>> {
        return proceedFlow { datasource.updateUserProfile(updateProfileRequest).toUpdateProfile() }
    }
}
