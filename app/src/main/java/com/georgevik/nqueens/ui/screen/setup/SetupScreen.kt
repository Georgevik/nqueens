package com.georgevik.nqueens.ui.screen.setup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.georgevik.nqueens.ui.theme.NQueensTheme

@Composable
fun SetupScreen(onStart: () -> Unit, onScore: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Game setup",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Configure your N-queens parameters before starting")

        Row {
            Button(onClick = onStart) {
                Text("Start Game")
            }

            Button(onClick = onScore) {
                Text("Scores")
            }
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
