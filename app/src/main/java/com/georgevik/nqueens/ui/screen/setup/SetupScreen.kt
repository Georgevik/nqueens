package com.georgevik.nqueens.ui.screen.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R
import com.georgevik.nqueens.ui.screen.setup.components.BoardSizePanel
import com.georgevik.nqueens.ui.screen.setup.components.HelpPanel
import com.georgevik.nqueens.ui.theme.NQueensTheme

private val PanelSpacing = 32.dp

@Composable
fun SetupScreen(onStart: () -> Unit, onScore: () -> Unit) {
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
                    stringResource(R.string.setup_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    stringResource(R.string.setup_subtitle),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            BoardSizePanel(
                onQueenUpdate = {}
            )

            HelpPanel(
                selectedIndex = 1,
                onSelectedIndex = {},
            )
        }

        SubmitButtons(
            modifier = Modifier.fillMaxWidth(),
            onScore = onScore,
            onStart = onStart
        )
    }
}

@Composable
fun SubmitButtons(modifier: Modifier, onStart: () -> Unit, onScore: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onStart) {
            Text(stringResource(R.string.setup_start_game))
        }

        TextButton(modifier = Modifier.padding(top = 8.dp), onClick = onScore) {
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
