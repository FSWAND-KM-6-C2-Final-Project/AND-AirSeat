package com.c2.airseat.data.datasource

import com.c2.airseat.data.model.BaseResponse
import com.c2.airseat.data.source.network.model.profile.ProfileData
import com.c2.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.c2.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface ProfileDataSource {
    suspend fun getUserProfile(): BaseResponse<ProfileData>

    suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): BaseResponse<Any>

    suspend fun deleteAccount(): BaseResponse<Any>
}

class ProfileDataSourceImpl(private val service: AirSeatApiServiceWithAuthorization) : ProfileDataSource {
    override suspend fun getUserProfile(): BaseResponse<ProfileData> {
        return service.getUserProfile()
    }

    override suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): BaseResponse<Any> {
        return service.updateProfile(updateProfileRequest)
    }

    override suspend fun deleteAccount(): BaseResponse<Any> {
        return service.deleteAccount()
    }
}
