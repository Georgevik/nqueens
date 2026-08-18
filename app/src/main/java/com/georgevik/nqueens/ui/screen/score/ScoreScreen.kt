package com.georgevik.nqueens.ui.screen.score

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ScoreScreen(onExit: () -> Unit) {
    Column {
        Text("Score Screen")
        Button(onClick = onExit) {
            Text("Exit")
        }
    }
}
