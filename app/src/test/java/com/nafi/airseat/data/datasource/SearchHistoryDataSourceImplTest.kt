package com.nafi.airseat.data.datasource

import app.cash.turbine.test
import com.nafi.airseat.data.source.local.database.dao.SearchHistoryDao
import com.nafi.airseat.data.source.local.database.entity.SearchHistoryEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchHistoryDataSourceImplTest {
    @MockK
    lateinit var searchHistoryDao: SearchHistoryDao

    private lateinit var searchHistoryDataSource: SearchHistoryDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        searchHistoryDataSource = SearchHistoryDataSourceImpl(searchHistoryDao)
    }

    @Test
    fun getAllSearchHistory() {
        val entity1 = mockk<SearchHistoryEntity>()
        val entity2 = mockk<SearchHistoryEntity>()
        val listEntity = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(listEntity)
            }

        every { searchHistoryDao.getAllSearchHistory() } returns mockFlow
        runTest {
            searchHistoryDataSource.getAllSearchHistory().test {
                val result = awaitItem()
                assertEquals(listEntity.size, result.size)
                assertEquals(entity1, result[0])
                assertEquals(entity2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun addSearchHistory() {
        runTest {
            val mockEntity = mockk<SearchHistoryEntity>()
            coEvery { searchHistoryDao.addSearchHistory(any()) } returns 1
            val result = searchHistoryDataSource.addSearchHistory(mockEntity)
            coVerify { searchHistoryDao.addSearchHistory(any()) }
            assertEquals(1, result)
        }
    }

    @Test
    fun deleteSearchHistory() {
        runTest {
            val mockEntity = mockk<SearchHistoryEntity>()
            coEvery { searchHistoryDao.deleteSearchHistory(any()) } returns 1
            val result = searchHistoryDataSource.deleteSearchHistory(mockEntity)
            coVerify { searchHistoryDao.deleteSearchHistory(any()) }
            assertEquals(1, result)
        }
    }

    @Test
    fun deleteAll() {
        runTest {
            coEvery { searchHistoryDao.deleteAll() } returns Unit
            val result = searchHistoryDataSource.deleteAll()
            coVerify { searchHistoryDao.deleteAll() }
            assertEquals(Unit, result)
        }
    }
}
