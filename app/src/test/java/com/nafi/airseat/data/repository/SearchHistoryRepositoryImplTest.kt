package com.nafi.airseat.data.repository

import app.cash.turbine.test
import com.nafi.airseat.data.datasource.SearchHistoryDataSource
import com.nafi.airseat.data.model.SearchHistory
import com.nafi.airseat.data.source.local.database.entity.SearchHistoryEntity
import com.nafi.airseat.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchHistoryRepositoryImplTest {
    @MockK
    lateinit var dataSource: SearchHistoryDataSource

    private lateinit var repository: SearchHistoryRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SearchHistoryRepositoryImpl(dataSource)
    }

    @Test
    fun `get searchHistory loading`() {
        val entity1 = mockk<SearchHistoryEntity>()
        val entity2 = mockk<SearchHistoryEntity>()
        val listEntity = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(listEntity)
            }

        every { dataSource.getAllSearchHistory() } returns mockFlow
        runTest {
            repository.getSearchHistory().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                verify { dataSource.getAllSearchHistory() }
            }
        }
    }

    @Test
    fun `get searchHistory success`() {
        val entity1 =
            SearchHistoryEntity(
                id = 12,
                searchHistory = "dakwjhdakw",
            )
        val entity2 =
            SearchHistoryEntity(
                id = 2,
                searchHistory = "dakwjhdawdfdakw",
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(mockList)
            }

        every { dataSource.getAllSearchHistory() } returns mockFlow
        runTest {
            repository.getSearchHistory().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(mockList.size, data.payload?.size)
                verify { dataSource.getAllSearchHistory() }
            }
        }
    }

    @Test
    fun `get searchHistory error`() {
        every { dataSource.getAllSearchHistory() } returns flow { throw IllegalStateException("Error") }
        runTest {
            repository.getSearchHistory().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                verify { dataSource.getAllSearchHistory() }
            }
        }
    }

    @Test
    fun `get searchHistory empty`() {
        val entity1 = mockk<SearchHistoryEntity>()
        val entity2 = mockk<SearchHistoryEntity>()
        val listEntity = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(listEntity)
            }

        every { dataSource.getAllSearchHistory() } returns mockFlow
        runTest {
            repository.getSearchHistory().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                verify { dataSource.getAllSearchHistory() }
            }
        }
    }

    @Test
    fun `create history loading`() {
        val mockSearchHistory = mockk<SearchHistory>(relaxed = true)
        every { mockSearchHistory.id } returns 1
        coEvery { dataSource.addSearchHistory(any()) } returns 1
        runTest {
            repository.createHistory(mockSearchHistory).map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.addSearchHistory(any()) }
            }
        }
    }

    @Test
    fun `create history success`() {
        val mockSearchHistory = mockk<SearchHistory>(relaxed = true)
        every { mockSearchHistory.id } returns 1
        coEvery { dataSource.addSearchHistory(any()) } returns 1
        runTest {
            repository.createHistory(mockSearchHistory).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                assertEquals(true, data.payload)
                coVerify { dataSource.addSearchHistory(any()) }
            }
        }
    }

    @Test
    fun `create history error`() {
        val mockSearchHistory = mockk<SearchHistory>(relaxed = true)
        every { mockSearchHistory.id } returns 1
        coEvery { dataSource.addSearchHistory(any()) } throws IllegalStateException("error")
        runTest {
            repository.createHistory(mockSearchHistory).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.addSearchHistory(any()) }
            }
        }
    }

    @Test
    fun `create history id null`() {
        val mockSearchHistory = mockk<SearchHistory>(relaxed = true)
        every { mockSearchHistory.id } returns null
        coEvery { dataSource.addSearchHistory(any()) } throws IllegalStateException("error")
        runTest {
            repository.createHistory(mockSearchHistory).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.addSearchHistory(any()) }
                coVerify(atLeast = 0) { dataSource.addSearchHistory(any()) }
            }
        }
    }

    @Test
    fun deleteSearchHistory() {
        val mockSearchHistory =
            SearchHistory(
                id = 1,
                searchHistory = "Jakarta",
            )
        coEvery { dataSource.deleteSearchHistory(any()) } returns 1
        runTest {
            repository.deleteSearchHistory(mockSearchHistory).map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify(atLeast = 1) { dataSource.deleteSearchHistory(any()) }
            }
        }
    }

    @Test
    fun deleteAll() {
        coEvery { dataSource.deleteAll() } returns Unit
        runTest {
            repository.deleteAll().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.deleteAll() }
            }
        }
    }
}
