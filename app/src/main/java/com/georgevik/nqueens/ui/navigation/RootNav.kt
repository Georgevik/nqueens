package com.georgevik.nqueens.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.georgevik.nqueens.ui.screen.game.model.GameScreen
import com.georgevik.nqueens.ui.screen.score.ScoreScreen
import com.georgevik.nqueens.ui.screen.setup.SetupScreen

private data object GameRoute : KeyRoute
private data object SetupRoute : KeyRoute
private data object ScoreRoute : KeyRoute

@Composable
fun RootNav(modifier: Modifier) {
    val backStack = remember { mutableStateListOf<KeyRoute>(ScoreRoute) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                GameRoute -> NavEntry(GameRoute) {
                    GameScreen(
                        onScore = {
                            backStack.removeLastOrNull()
                            backStack.add(ScoreRoute)
                        })
                }

                ScoreRoute -> NavEntry(ScoreRoute) { ScoreScreen() }
                SetupRoute -> NavEntry(SetupRoute) {
                    SetupScreen(
                        onStart = { backStack.add(GameRoute) },
                        onScore = { backStack.add(ScoreRoute) })
                }
            }
        })
}
