package com.georgevik.nqueens.ui.screen.score

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.georgevik.nqueens.ui.theme.NQueensTheme

@Composable
fun ScoreScreen(onExit: () -> Unit) {
    Column {
        Text("Score Screen")
        Button(onClick = onExit) {
            Text("Exit")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScoreScreenPreview() {
    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ScoreScreen(onExit = {})
            }
        }
    }
}
