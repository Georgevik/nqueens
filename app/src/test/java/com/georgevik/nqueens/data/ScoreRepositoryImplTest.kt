package com.georgevik.nqueens.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.georgevik.nqueens.data.storage.ScoreDao
import com.georgevik.nqueens.data.storage.ScoreEntity
import com.georgevik.nqueens.domain.model.Score
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

class ScoreRepositoryImplTest {

    private val scoreDao: ScoreDao = mockk()
    private lateinit var underTest: ScoreRepositoryImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        underTest = ScoreRepositoryImpl(scoreDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `inserts the mapped entity WHEN a score is saved`() = runTest {
        coEvery { scoreDao.insert(any()) } returns Unit
        val score = Score(
            id = "abc",
            playedAt = Instant.ofEpochMilli(1_000L),
            nQueens = 8,
            timeConsumed = 42_000L,
            attempts = 3,
        )

        underTest.insertScore(score)

        coVerify {
            scoreDao.insert(
                ScoreEntity(
                    id = "abc",
                    playedAt = 1_000L,
                    nQueens = 8,
                    timeConsumedMillis = 42_000L,
                    attempts = 3,
                )
            )
        }
    }

    @Test
    fun `emits domain scores mapped from entities WHEN scores are collected`() = runTest {
        val entities = listOf(
            ScoreEntity(
                id = "1",
                playedAt = 5_000L,
                nQueens = 4,
                timeConsumedMillis = 100L,
                attempts = 1
            ),
            ScoreEntity(
                id = "2",
                playedAt = 6_000L,
                nQueens = 6,
                timeConsumedMillis = 200L,
                attempts = 2
            ),
        )
        every { scoreDao.scorePaged() } returns pagingSourceOf(entities)

        val scores = underTest.scores().asSnapshot()

        assertEquals(
            listOf(
                Score("1", Instant.ofEpochMilli(5_000L), 4, 100L, 1),
                Score("2", Instant.ofEpochMilli(6_000L), 6, 200L, 2),
            ),
            scores,
        )
    }

    private fun pagingSourceOf(items: List<ScoreEntity>) =
        object : PagingSource<Int, ScoreEntity>() {
            override fun getRefreshKey(state: PagingState<Int, ScoreEntity>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ScoreEntity> =
                LoadResult.Page(data = items, prevKey = null, nextKey = null)
        }
}
