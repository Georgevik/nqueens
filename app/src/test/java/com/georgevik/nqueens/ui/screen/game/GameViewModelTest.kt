package com.georgevik.nqueens.ui.screen.game

import app.cash.turbine.test
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.domain.model.Position
import com.georgevik.nqueens.domain.repository.ScoreRepository
import com.georgevik.nqueens.domain.usecase.IsValidQueenPosition
import com.georgevik.nqueens.ui.navigation.model.GameConfig
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

        underTest.uiEvent.test {
            underTest.submit()
            assertEquals(GameEvent.Success, awaitItem())
        }
        coVerify { scoreRepository.insertScore(any()) }
    }

    @Test
    fun `emits MissingQueens and does not save WHEN not all queens are placed`() = runTest {
        val underTest = viewModel(nQueens = 4)

        underTest.uiEvent.test {
            underTest.submit()
            assertEquals(GameEvent.MissingQueens, awaitItem())
        }
        assertEquals(1, underTest.uiState.value.attempts)
        coVerify(exactly = 0) { scoreRepository.insertScore(any()) }
    }

    @Test
    fun `emits QueensInWrongPosition WHEN queens are placed but invalid`() = runTest {
        val underTest = viewModel(nQueens = 1)
        underTest.cellPressed(Position(0, 0))
        every { isValidQueenPosition(any(), any(), any()) } returns false

        underTest.uiEvent.test {
            underTest.submit()
            assertEquals(GameEvent.QueensInWrongPosition, awaitItem())
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
