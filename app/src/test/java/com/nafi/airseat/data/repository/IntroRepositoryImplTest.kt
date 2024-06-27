package com.nafi.airseat.data.repository

import com.nafi.airseat.data.datasource.intro.IntroDataSource
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IntroRepositoryImplTest {
    @MockK
    lateinit var ds: IntroDataSource

    private lateinit var pref: IntroRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        pref = IntroRepositoryImpl(ds)
    }

    @Test
    fun isAppIntroShown() {
        every { ds.isAppIntroShown() } returns true
        val actualResult = pref.isAppIntroShown()
        verify { ds.isAppIntroShown() }
        assertEquals(true, actualResult)
    }

    @Test
    fun setAppIntroShown() {
        every { ds.setAppIntroShown(any()) } returns Unit
        pref.setAppIntroShown(true)
        verify { ds.setAppIntroShown(any()) }
    }
}
