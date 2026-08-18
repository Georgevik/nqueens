package com.georgevik.nqueens.ui.screen.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.georgevik.nqueens.ui.theme.NQueensTheme

@Composable
fun GameScreen(onScore: () -> Unit, onBack: () -> Unit) {
    Column {
        Text("Game Screen")
        Row {
            Button(onClick = onScore) {
                Text("Score")
            }

            Button(onClick = onBack) {
                Text("Exit")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                GameScreen(onScore = {}, onBack = {})
            }
        }
    }
}
