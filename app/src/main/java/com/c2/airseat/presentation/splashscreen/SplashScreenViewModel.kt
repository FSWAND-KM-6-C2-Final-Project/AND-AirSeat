package com.c2.airseat.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.c2.airseat.data.repository.IntroRepository

class SplashScreenViewModel(
    private val introRepository: IntroRepository,
) : ViewModel() {
    fun setAppIntroShown(isShown: Boolean) = introRepository.setAppIntroShown(isShown)
}
