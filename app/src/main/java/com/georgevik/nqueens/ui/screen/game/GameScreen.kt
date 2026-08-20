package com.georgevik.nqueens.ui.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.domain.model.Position
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import com.georgevik.nqueens.ui.screen.game.component.Board
import com.georgevik.nqueens.ui.screen.game.component.GameControls
import com.georgevik.nqueens.ui.screen.game.component.VictoryDialog
import com.georgevik.nqueens.ui.screen.game.model.Cell
import com.georgevik.nqueens.ui.screen.game.model.GameUi
import com.georgevik.nqueens.ui.theme.NQueensTheme

@Composable
fun GameScreen(
    config: GameConfig,
    onNewGame: () -> Unit,
    onScore: () -> Unit,
) {
    val vm: GameViewModel = hiltViewModel<GameViewModel, GameViewModel.Factory> { factory ->
        factory.create(config)
    }
    val ui by vm.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = rememberLifecycleOwner()

    LaunchedEffect(vm) {
        vm.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                GameEvent.Score -> onScore()
                GameEvent.StartGame -> onNewGame()
            }
        }
    }

    GameScreenContent(
        config = config,
        ui = ui,
        highlightQueensLeft = ui.highlightQueensLeft,
        conflictBanner = ui.showConflict,
        onUserAction = { action ->
            when (action) {
                UserAction.Reset -> vm.reset()
                is UserAction.CellPressed -> vm.cellPressed(action.position)
                UserAction.Submit -> vm.submit()
            }
        })

    if (ui.showVictory) {
        VictoryDialog(
            onNewGame = { vm.onNewGame() },
            onViewScores = { vm.onScore() },
        )
    }
}

@Composable
private fun GameScreenContent(
    config: GameConfig,
    ui: GameUi,
    highlightQueensLeft: Boolean = false,
    conflictBanner: Boolean = true,
    onUserAction: (UserAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Board(
                size = config.nQueens,
                markedCells = ui.markedCells,
                onCellPress = { onUserAction(UserAction.CellPressed(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }

        GameControls(
            startTime = ui.startTime,
            attempts = ui.attempts,
            queensLeft = (config.nQueens - ui.queens.size).coerceAtLeast(0),
            highlightQueensLeft = highlightQueensLeft,
            conflictBanner = conflictBanner,
            onReset = { onUserAction(UserAction.Reset) },
            onSubmit = { onUserAction(UserAction.Submit) },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    val gameUi = GameUi(
        attempts = 3,
        markedCells = listOf(
            Cell(Position(col = 0, row = 0), isQueen = true, showError = false),
            Cell(Position(col = 5, row = 4), isQueen = true, showError = true),
        ),
        startTime = System.currentTimeMillis(),
        highlightQueensLeft = false,
        showConflict = false,
        showVictory = false,
    )

    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                GameScreenContent(
                    config = GameConfig(8, HelpLevel.NONE),
                    ui = gameUi,
                    onUserAction = {})
            }
        }
    }
}

private sealed interface UserAction {
    data object Submit : UserAction
    data object Reset : UserAction
    data class CellPressed(val position: Position) : UserAction
}
