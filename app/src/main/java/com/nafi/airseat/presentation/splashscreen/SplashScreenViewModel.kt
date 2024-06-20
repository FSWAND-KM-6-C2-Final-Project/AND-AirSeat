package com.nafi.airseat.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.repository.IntroRepository

class SplashScreenViewModel(
    private val introRepository: IntroRepository,
) : ViewModel() {
    fun setAppIntroShown(isShown: Boolean) = introRepository.setAppIntroShown(isShown)
}
