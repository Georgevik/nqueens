package com.georgevik.nqueens.ui.screen.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GameScreen(modifier: Modifier = Modifier, onScore: () -> Unit, onBack: () -> Unit) {
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
