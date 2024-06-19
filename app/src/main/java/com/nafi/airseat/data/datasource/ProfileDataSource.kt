package com.nafi.airseat.data.datasource

import com.nafi.airseat.data.source.network.model.profile.ProfileResponse
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileRequest
import com.nafi.airseat.data.source.network.model.profile.UpdateProfileResponse
import com.nafi.airseat.data.source.network.services.AirSeatApiServiceWithAuthorization

interface ProfileDataSource {
    suspend fun getUserProfile(): ProfileResponse

    suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): UpdateProfileResponse
}

class ProfileDataSourceImpl(private val service: AirSeatApiServiceWithAuthorization) : ProfileDataSource {
    override suspend fun getUserProfile(): ProfileResponse {
        return service.getUserProfile()
    }

    override suspend fun updateUserProfile(updateProfileRequest: UpdateProfileRequest): UpdateProfileResponse {
        return service.updateProfile(updateProfileRequest)
    }
}
