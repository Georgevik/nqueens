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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val gameConfig: GameConfig,
    private val isValidQueenPosition: IsValidQueenPosition,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(newGameUI)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<GameEvent>()
    val uiEvent = _uiEvent.consumeAsFlow()

    private val newGameUI
        get() = GameUi(
            attempts = 0,
            markedCells = emptyList(),
            startTime = System.currentTimeMillis(),
        )

    fun reset() {
        _uiState.update { newGameUI }
    }

    fun cellPressed(position: Position) {
        val isQueen = _uiState.value.queens.any { it.position == position }

        if (isQueen) {
            removeQueen(position)
        } else {
            addQueen(position)
        }
    }

    private fun addQueen(position: Position) {
        if (_uiState.value.queens.size >= gameConfig.nQueens) return

        _uiState.update { ui ->
            val newCells = ui.markedCells.toMutableList()
            newCells.add(Cell(position, isQueen = true))
            ui.copy(markedCells = calculateCellFloors(newCells))
        }
    }

    private fun removeQueen(position: Position) {
        _uiState.update { ui ->
            val newCells = ui.markedCells.filterNot { it.isQueen && it.position == position }

            if (gameConfig.helpLevel == HelpLevel.NONE) {
                ui.copy(markedCells = newCells)
            } else {
                ui.copy(markedCells = calculateCellFloors(newCells))
            }
        }
    }


    fun submit() = viewModelScope.launch {
        val queens = uiState.value.queens.map { it.position }.toSet()
        if (queens.size != gameConfig.nQueens) {
            _uiState.update { it.copy(attempts = it.attempts + 1) }
            _uiEvent.send(GameEvent.MissingQueens)
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
            _uiEvent.send(GameEvent.Success)
        } else {
            _uiState.update { it.copy(attempts = it.attempts + 1) }
            _uiEvent.send(GameEvent.QueensInWrongPosition)
        }
    }

    private fun calculateCellFloors(allCell: List<Cell>): List<Cell> {
        val queens = allCell.filter { it.isQueen }.map { it.position }.toSet()

        return allCell.map { cell ->
            val isError = !isValidQueenPosition(
                queen = cell.position,
                nQueenSize = gameConfig.nQueens,
                boardQueens = queens
            )

            cell.copy(showError = isError)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(gameConfig: GameConfig): GameViewModel
    }
}

sealed interface GameEvent {
    data object MissingQueens : GameEvent
    data object QueensInWrongPosition : GameEvent
    data object Success : GameEvent
}
