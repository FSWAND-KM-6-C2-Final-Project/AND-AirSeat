package com.c2.airseat.presentation.appintro

import androidx.lifecycle.ViewModel
import com.c2.airseat.data.source.local.pref.UserPreference

class AppIntroViewModel(private val introRepository: UserPreference) : ViewModel() {
    fun isAppIntroShown() = introRepository.isAppIntroShown()
}
