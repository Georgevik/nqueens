package com.georgevik.nqueens.ui.screen.setup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SetupScreen(onStart: () -> Unit, onScore: () -> Unit) {
    Column {
        Text("GameSetup Screen")

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
