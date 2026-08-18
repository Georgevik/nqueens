package com.georgevik.nqueens.ui.screen.game.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.ui.screen.game.GameUi
import com.georgevik.nqueens.ui.screen.game.component.Board
import com.georgevik.nqueens.ui.screen.game.component.GameControls
import com.georgevik.nqueens.ui.theme.NQueensTheme

@Composable
fun GameScreen(
    config: GameConfig = GameConfig(8, HelpLevel.ERROR_ONLY),
    ui: GameUi = GameUi(
        attempts = 3,
        queens = listOf(
            Queen(col = 0, row = 0, showError = false),
            Queen(col = 5, row = 4, showError = true),
        ),
        startTime = System.currentTimeMillis(),
    ),
    onScore: () -> Unit,
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
                queens = ui.queens,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }

        GameControls(
            startTime = ui.startTime,
            attempts = ui.attempts,
            queensLeft = (config.nQueens - ui.queens.size).coerceAtLeast(0),
            onReset = { TODO() },
            onSubmit = { TODO() },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                GameScreen(onScore = {})
            }
        }
    }
}
