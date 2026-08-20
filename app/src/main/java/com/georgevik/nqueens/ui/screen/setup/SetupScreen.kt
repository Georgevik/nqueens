package com.georgevik.nqueens.ui.screen.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import com.georgevik.nqueens.ui.screen.setup.components.BoardSizePanel
import com.georgevik.nqueens.ui.screen.setup.components.HelpPanel
import com.georgevik.nqueens.ui.theme.NQueensTheme

private val PanelSpacing = 32.dp
private const val MIN_QUEENS = 4
private const val MAX_QUEENS = 12

@Composable
fun SetupScreen(onStart: (GameConfig) -> Unit, onScore: () -> Unit) {
    val queenRange: IntRange = remember { MIN_QUEENS..MAX_QUEENS }
    var nQueens by rememberSaveable { mutableIntStateOf((queenRange.last-queenRange.first) / 2 + queenRange.first) }
    var helpLevel by rememberSaveable { mutableStateOf(HelpLevel.ERROR_ONLY) }

    Column {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(32.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PanelSpacing),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "♛",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    stringResource(R.string.setup_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                )

                Text(
                    stringResource(R.string.setup_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            BoardSizePanel(
                nQueens = nQueens,
                onQueenUpdate = { nQueens = it },
                range = queenRange,
            )

            HelpPanel(
                selected = helpLevel,
                onSelected = { helpLevel = it },
            )
        }

        SubmitButtons(
            modifier = Modifier.fillMaxWidth(),
            onScore = onScore,
            onStart = {
                onStart(GameConfig(nQueens = nQueens, helpLevel = helpLevel))
            },
        )
    }
}

@Composable
fun SubmitButtons(modifier: Modifier, onStart: () -> Unit, onScore: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Text(
                stringResource(R.string.setup_start_game),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        TextButton(
            onClick = onScore,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        ) {
            Text(stringResource(R.string.setup_scores))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SetupScreenPreview() {
    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                SetupScreen(onStart = {}, onScore = {})
            }
        }
    }
}
