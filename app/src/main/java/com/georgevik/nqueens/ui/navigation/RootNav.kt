package com.georgevik.nqueens.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.georgevik.nqueens.ui.screen.game.GameScreen
import com.georgevik.nqueens.ui.screen.score.ScoreScreen
import com.georgevik.nqueens.ui.screen.setup.SetupScreen

private data object GameRoute : KeyRoute
private data object SetupRoute : KeyRoute
private data object ScoreRoute : KeyRoute

@Composable
fun RootNav() {
    val backStack = remember { mutableStateListOf<KeyRoute>(SetupRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                GameRoute -> NavEntry(GameRoute) {
                    GameScreen(
                        onBack = { backStack.removeLastOrNull() },
                        onScore = {
                            backStack.removeLastOrNull()
                            backStack.add(ScoreRoute)
                        })
                }

                ScoreRoute -> NavEntry(ScoreRoute) {
                    ScoreScreen(onExit = { backStack.removeLastOrNull() })
                }

                SetupRoute -> NavEntry(SetupRoute) {
                    SetupScreen(
                        onStart = { backStack.add(GameRoute) },
                        onScore = { backStack.add(ScoreRoute) })
                }
            }
        }
    )
}
