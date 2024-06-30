package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.local.pref.UserPreference

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
