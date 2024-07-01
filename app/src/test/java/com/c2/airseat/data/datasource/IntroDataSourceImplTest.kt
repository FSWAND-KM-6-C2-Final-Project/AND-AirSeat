package com.c2.airseat.data.datasource

import com.c2.airseat.data.source.local.pref.UserPreference
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IntroDataSourceImplTest {
    private lateinit var userPreference: UserPreference
    private lateinit var introDataSource: IntroDataSourceImpl

    @Before
    fun setUp() {
        userPreference = mockk()
        introDataSource = IntroDataSourceImpl(userPreference)
    }

    @Test
    fun isAppIntroShown() =
        runBlocking {
            coEvery { userPreference.isAppIntroShown() } returns true

            val result = introDataSource.isAppIntroShown()

            assertEquals(true, result)
            coVerify { userPreference.isAppIntroShown() }
        }

    @Test
    fun setAppIntroShown() =
        runBlocking {
            coEvery { userPreference.setAppIntroShown(true) } returns Unit
            introDataSource.setAppIntroShown(true)

            coVerify { userPreference.setAppIntroShown(true) }
        }
}
