package com.georgevik.nqueens.ui.screen.game

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.domain.model.Position
import com.georgevik.nqueens.domain.model.Score
import com.georgevik.nqueens.domain.repository.ScoreRepository
import com.georgevik.nqueens.domain.usecase.IsValidQueenPosition
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private val isValidQueenPosition: IsValidQueenPosition = mockk()
    private val scoreRepository: ScoreRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { isValidQueenPosition(any(), any(), any()) } returns true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(
        nQueens: Int = 4,
        helpLevel: HelpLevel = HelpLevel.ERROR_ONLY,
    ) = GameViewModel(GameConfig(nQueens, helpLevel), isValidQueenPosition, scoreRepository)

    @Test
    fun `adds a queen WHEN an empty cell is pressed`() {
        val underTest = viewModel()

        underTest.cellPressed(Position(0, 0))

        val queens = underTest.uiState.value.queens
        assertEquals(1, queens.size)
        assertEquals(Position(0, 0), queens.first().position)
    }

    @Test
    fun `removes the queen WHEN a cell with a queen is pressed`() {
        val underTest = viewModel()
        underTest.cellPressed(Position(0, 0))

        underTest.cellPressed(Position(0, 0))

        assertTrue(underTest.uiState.value.queens.isEmpty())
    }

    @Test
    fun `ignores the press WHEN the queen limit is reached`() {
        val underTest = viewModel(nQueens = 1)
        underTest.cellPressed(Position(0, 0))

        underTest.cellPressed(Position(1, 1))

        assertEquals(1, underTest.uiState.value.queens.size)
    }

    @Test
    fun `hides victory WHEN new game is launched`() = runTest {
        val underTest = viewModel(nQueens = 1)

        turbineScope {
            val ui = underTest.uiState.testIn(backgroundScope)
            val events = underTest.uiEvent.testIn(backgroundScope)

            underTest.onNewGame()

            with(ui.awaitItem()) { assertEquals(false, showVictory) }
            with(events.awaitItem()) { assertTrue(this is GameEvent.StartGame) }
        }
    }

    @Test
    fun `hides victory WHEN score is launched`() = runTest {
        val underTest = viewModel(nQueens = 1)

        turbineScope {
            val ui = underTest.uiState.testIn(backgroundScope)
            val events = underTest.uiEvent.testIn(backgroundScope)

            underTest.onScore()

            with(ui.awaitItem()) { assertEquals(false, showVictory) }
            with(events.awaitItem()) { assertTrue(this is GameEvent.Score) }
        }
    }

    @Test
    fun `marks the queen with error WHEN it conflicts and help is not none`() {
        every { isValidQueenPosition(any(), any(), any()) } returns false
        val underTest = viewModel(helpLevel = HelpLevel.ERROR_ONLY)

        underTest.cellPressed(Position(0, 0))

        assertTrue(underTest.uiState.value.queens.first().showError)
    }

    @Test
    fun `highlights the attacked empty cell WHEN help is full`() {
        val underTest = viewModel(nQueens = 4, helpLevel = HelpLevel.FULL)
        every { isValidQueenPosition(Position(1, 0), 4, any()) } returns false

        underTest.cellPressed(Position(0, 0))

        val blocked = underTest.uiState.value.markedCells.filter { it.isAttacked }
        assertEquals(1, blocked.size)
        assertEquals(Position(1, 0), blocked.first().position)
    }

    @Test
    fun `emits Success and saves the score WHEN a valid solution is submitted`() = runTest {
        val underTest = viewModel(nQueens = 1)
        underTest.cellPressed(Position(0, 0))

        underTest.uiState.test {
            skipItems(1)
            underTest.submit()
            assertEquals(true, awaitItem().showVictory)
        }
        coVerify { scoreRepository.insertScore(any()) }
    }

    @Test
    fun `has null timeConsumedMillis initially`() {
        val underTest = viewModel()

        assertNull(underTest.uiState.value.timeConsumedMillis)
    }

    @Test
    fun `sets timeConsumedMillis to the saved score time WHEN a valid solution is submitted`() =
        runTest {
            val underTest = viewModel(nQueens = 1)
            underTest.cellPressed(Position(0, 0))
            val savedScore = slot<Score>()

            underTest.uiState.test {
                skipItems(1)
                underTest.submit()
                assertEquals(true, awaitItem().showVictory)
            }

            coVerify { scoreRepository.insertScore(capture(savedScore)) }
            val time = underTest.uiState.value.timeConsumedMillis
            assertEquals(savedScore.captured.timeConsumed, time)
        }

    @Test
    fun `keeps timeConsumedMillis null WHEN queens are placed but invalid`() = runTest {
        val underTest = viewModel(nQueens = 1)
        underTest.cellPressed(Position(0, 0))
        every { isValidQueenPosition(any(), any(), any()) } returns false

        underTest.submit()

        assertNull(underTest.uiState.value.timeConsumedMillis)
    }

    @Test
    fun `emits highlightQueensLeft WHEN not all queens are placed`() = runTest {
        val underTest = viewModel(nQueens = 4)

        underTest.uiState.test {
            skipItems(1)
            underTest.submit()

            with(awaitItem()) {
                assertEquals(attempts, 1)
                assertEquals(highlightQueensLeft, true)
            }

            with(awaitItem()) {
                assertEquals(attempts, 1)
                assertEquals(highlightQueensLeft, false)
            }
        }
        assertEquals(1, underTest.uiState.value.attempts)
        coVerify(exactly = 0) { scoreRepository.insertScore(any()) }
    }

    @Test
    fun `emits showConflict WHEN queens are placed but invalid`() = runTest {
        val underTest = viewModel(nQueens = 1)
        underTest.cellPressed(Position(0, 0))
        every { isValidQueenPosition(any(), any(), any()) } returns false

        underTest.uiState.test {
            skipItems(1)
            underTest.submit()

            with(awaitItem()) {
                assertEquals(attempts, 1)
                assertEquals(showConflict, true)
            }

            with(awaitItem()) {
                assertEquals(attempts, 1)
                assertEquals(showConflict, false)
            }
        }
        coVerify(exactly = 0) { scoreRepository.insertScore(any()) }
    }

    @Test
    fun `clears the board WHEN reset is called`() {
        val underTest = viewModel()
        underTest.cellPressed(Position(0, 0))

        underTest.reset()

        assertTrue(underTest.uiState.value.queens.isEmpty())
        assertEquals(0, underTest.uiState.value.attempts)
    }
}
