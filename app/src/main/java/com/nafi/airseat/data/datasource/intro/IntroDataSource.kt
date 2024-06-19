package com.nafi.airseat.data.datasource.intro

import com.nafi.airseat.data.source.local.pref.UserPreference

interface IntroDataSource {
    fun isAppIntroShown(): Boolean

    fun setAppIntroShown(isShown: Boolean)
}

class IntroDataSourceImpl(private val userPreference: UserPreference) : IntroDataSource {
    override fun isAppIntroShown(): Boolean {
        return userPreference.isAppIntroShown()
    }

    override fun setAppIntroShown(isShown: Boolean) {
        return userPreference.setAppIntroShown(isShown)
    }
}
