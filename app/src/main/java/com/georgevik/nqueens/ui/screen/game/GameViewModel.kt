package com.georgevik.nqueens.ui.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.domain.model.Position
import com.georgevik.nqueens.domain.model.Score
import com.georgevik.nqueens.domain.repository.ScoreRepository
import com.georgevik.nqueens.domain.usecase.IsValidQueenPosition
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import com.georgevik.nqueens.ui.screen.game.model.Cell
import com.georgevik.nqueens.ui.screen.game.model.GameUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val gameConfig: GameConfig,
    private val isValidQueenPosition: IsValidQueenPosition,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUi.create())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<GameEvent>()
    val uiEvent = _uiEvent.consumeAsFlow()

    fun reset() {
        _uiState.update { GameUi.create() }
    }

    fun cellPressed(position: Position) {
        val queens = _uiState.value.queens.map { it.position }.toSet()

        val newQueens = if (position in queens) {
            queens - position
        } else {
            if (queens.size >= gameConfig.nQueens) return
            queens + position
        }

        _uiState.update { it.copy(markedCells = buildMarkedCells(newQueens)) }
    }


    fun submit() = viewModelScope.launch {
        val queens = uiState.value.queens.map { it.position }.toSet()
        if (queens.size != gameConfig.nQueens) {
            _uiState.update { it.copy(attempts = it.attempts + 1, highlightQueensLeft = true) }
            delay(DELAY_HIGHLIGHT_LEFT_QUEENS)
            _uiState.update { it.copy(highlightQueensLeft = false) }
            return@launch
        }

        if (queens.all {
                isValidQueenPosition(
                    queen = it,
                    nQueenSize = gameConfig.nQueens,
                    boardQueens = queens
                )
            }) {
            scoreRepository.insertScore(
                Score(
                    id = UUID.randomUUID().toString(),
                    playedAt = Instant.now(),
                    nQueens = gameConfig.nQueens,
                    timeConsumed = System.currentTimeMillis() - uiState.value.startTime,
                    attempts = uiState.value.attempts
                )
            )
            _uiEvent.send(GameEvent.StartGame)
            _uiState.update { it.copy(showVictory = true) }

        } else {
            _uiState.update { it.copy(attempts = it.attempts + 1, showConflict = true) }
            delay(DELAY_BANNER)
            _uiState.update { it.copy(showConflict = false) }
        }
    }

    private fun buildMarkedCells(queens: Set<Position>): List<Cell> {
        val help = gameConfig.helpLevel
        val boardSize = gameConfig.nQueens

        val queenCells = queens.map { position ->
            val showError = help != HelpLevel.NONE && !isValidQueenPosition(
                queen = position,
                nQueenSize = boardSize,
                boardQueens = queens,
            )
            Cell(position = position, isQueen = true, showError = showError)
        }

        if (help != HelpLevel.FULL) return queenCells

        val attackedCells = buildList {
            (0 until boardSize).forEach { row ->
                (0 until boardSize).forEach { col ->
                    val position = Position(col = col, row = row)
                    if (position in queens) return@forEach
                    val attacked = !isValidQueenPosition(
                        queen = position,
                        nQueenSize = boardSize,
                        boardQueens = queens,
                    )
                    if (attacked) {
                        add(Cell(position = position, isQueen = false, isAttacked = true))
                    }
                }
            }
        }
        return queenCells + attackedCells
    }

    @AssistedFactory
    interface Factory {
        fun create(gameConfig: GameConfig): GameViewModel
    }

    companion object {
        private val DELAY_BANNER = 2.seconds
        private val DELAY_HIGHLIGHT_LEFT_QUEENS = 500.milliseconds
    }
}

sealed interface GameEvent {
    data object StartGame : GameEvent
}
