package com.nafi.airseat.data.datasource

import app.cash.turbine.test
import com.nafi.airseat.data.source.local.database.dao.AirportHistoryDao
import com.nafi.airseat.data.source.local.database.entity.AirportHistoryEntity
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

class AirportHistoryDataSourceImplTest {
    @MockK
    lateinit var airportHistoryDao: AirportHistoryDao

    private lateinit var airportHistoryDataSource: AirportHistoryDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        airportHistoryDataSource = AirportHistoryDataSourceImpl(airportHistoryDao)
    }

    @Test
    fun getAllAirportHistory() {
        val entity1 = mockk<AirportHistoryEntity>()
        val entity2 = mockk<AirportHistoryEntity>()
        val listEntity = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(listEntity)
            }
        every { airportHistoryDao.getAllAirportHistory() } returns mockFlow
        runTest {
            airportHistoryDataSource.getAllAirportHistory().test {
                val result = awaitItem()
                assertEquals(result.size, listEntity.size)
                assertEquals(entity1, result[0])
                assertEquals(entity2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun addAirportHistory() {
        runTest {
            val mockEntity = mockk<AirportHistoryEntity>()
            coEvery { airportHistoryDao.addAirportHistory(any()) } returns 1
            val result = airportHistoryDataSource.addAirportHistory(mockEntity)
            coVerify { airportHistoryDao.addAirportHistory(any()) }
            assertEquals(1, result)
        }
    }

    @Test
    fun deleteAirportHistory() {
        runTest {
            val mockEntity = mockk<AirportHistoryEntity>()
            coEvery { airportHistoryDao.deleteAirportHistory(any()) } returns 1
            val result = airportHistoryDataSource.deleteAirportHistory(mockEntity)
            coVerify { airportHistoryDao.deleteAirportHistory(any()) }
            assertEquals(1, result)
        }
    }

    @Test
    fun deleteAll() {
        runTest {
            coEvery { airportHistoryDao.deleteAll() } returns Unit
            val result = airportHistoryDataSource.deleteAll()
            coVerify { airportHistoryDao.deleteAll() }
            assertEquals(Unit, result)
        }
    }
}
