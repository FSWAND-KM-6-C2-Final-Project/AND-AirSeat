package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.intro.IntroDataSource

interface IntroRepository {
    fun isAppIntroShown(): Boolean

    fun setAppIntroShown(isShown: Boolean)
}

class IntroRepositoryImpl(private val dataSource: IntroDataSource) : IntroRepository {
    override fun isAppIntroShown(): Boolean {
        return dataSource.isAppIntroShown()
    }

    override fun setAppIntroShown(isShown: Boolean) {
        return dataSource.setAppIntroShown(isShown)
    }
}
