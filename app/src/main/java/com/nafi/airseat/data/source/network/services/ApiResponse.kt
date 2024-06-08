package com.nafi.airseat.data.source.network.services

sealed class ApiResponse<out T> {
    object Loading : ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : ApiResponse<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int,
    ) : ApiResponse<Nothing>()
}
