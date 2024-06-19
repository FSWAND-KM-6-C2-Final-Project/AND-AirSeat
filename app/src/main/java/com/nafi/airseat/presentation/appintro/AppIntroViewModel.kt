package com.nafi.airseat.presentation.appintro

import androidx.lifecycle.ViewModel
import com.nafi.airseat.data.source.local.pref.UserPreference

class AppIntroViewModel(private val introRepository: UserPreference) : ViewModel() {
    fun isAppIntroShown() = introRepository.isAppIntroShown()
}
