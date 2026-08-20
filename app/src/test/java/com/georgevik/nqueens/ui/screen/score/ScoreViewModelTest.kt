package com.georgevik.nqueens.ui.screen.score

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.georgevik.nqueens.domain.model.Score
import com.georgevik.nqueens.domain.repository.ScoreRepository
import com.georgevik.nqueens.infra.extensions.toUiFormat
import com.georgevik.nqueens.ui.screen.score.model.ScoreboardRowUi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

class ScoreViewModelTest {

    private val scoreRepository: ScoreRepository = mockk()

    private val underTest = ScoreViewModel(scoreRepository)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `maps repository scores to scoreboard rows WHEN collected`() = runTest {
        val score = Score(
            id = "7",
            playedAt = Instant.ofEpochMilli(1_000L),
            nQueens = 8,
            timeConsumed = 42_000L,
            attempts = 3,
        )
        every { scoreRepository.scores() } returns pagedFlowOf(listOf(score))

        val rows = underTest.scores.asSnapshot()

        assertEquals(
            listOf(
                ScoreboardRowUi(
                    id = "7",
                    date = score.playedAt.toUiFormat(),
                    nQueens = 8,
                    timeConsumed = "00:00:42:0",
                    attempts = 3,
                )
            ),
            rows,
        )
        underTest.viewModelScope.cancel()
    }

    @Test
    fun `emits an empty list WHEN the repository has no scores`() = runTest {
        every { scoreRepository.scores() } returns pagedFlowOf(emptyList())

        val rows = underTest.scores.asSnapshot()

        assertEquals(emptyList<ScoreboardRowUi>(), rows)
        underTest.viewModelScope.cancel()
    }

    private fun pagedFlowOf(scores: List<Score>) =
        Pager(PagingConfig(pageSize = 20)) {
            object : PagingSource<Int, Score>() {
                override fun getRefreshKey(state: PagingState<Int, Score>): Int? = null
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Score> =
                    LoadResult.Page(data = scores, prevKey = null, nextKey = null)
            }
        }.flow
}
