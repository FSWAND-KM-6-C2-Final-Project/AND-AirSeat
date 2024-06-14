package com.nafi.airseat.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.model.FavoriteDestination
import com.nafi.airseat.data.repository.FavoriteDestinationRepository

class HomeViewModel(
    private val favoriteDestinationRepository: FavoriteDestinationRepository,
    // private val userRepository: UserRepository,
) : ViewModel() {
    /*fun getCurrentUser() =
        userRepository
            .getCurrentUser()

    fun isUserLoggedOut() = userRepository.doLogout()
*/
    fun getFavoriteDestinations(): LiveData<List<FavoriteDestination>> {
        return favoriteDestinationRepository.getFavoriteDestinations()
    }
}
