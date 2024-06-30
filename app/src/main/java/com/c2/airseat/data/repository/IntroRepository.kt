package com.c2.airseat.data.repository

import com.c2.airseat.data.datasource.IntroDataSource

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
