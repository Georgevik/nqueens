package com.georgevik.nqueens.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import com.georgevik.nqueens.ui.screen.game.model.GameScreen
import com.georgevik.nqueens.ui.screen.score.ScoreScreen
import com.georgevik.nqueens.ui.screen.setup.SetupScreen

private data class GameRoute(val config: GameConfig) : KeyRoute
private data object SetupRoute : KeyRoute
private data object ScoreRoute : KeyRoute

@Composable
fun RootNav(modifier: Modifier) {
    val backStack = remember { mutableStateListOf<KeyRoute>(SetupRoute) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<GameRoute> {
                GameScreen(
                    config = it.config, onScore = {
                        backStack.removeLastOrNull()
                        backStack.add(ScoreRoute)
                    })
            }
            entry<ScoreRoute> { ScoreScreen() }
            entry<SetupRoute> {
                SetupScreen(
                    onStart = { config -> backStack.add(GameRoute(config)) },
                    onScore = { backStack.add(ScoreRoute) })
            }
        },
        transitionSpec = {
            ContentTransform(slideInHorizontally { it }, slideOutHorizontally { -it })
        }, popTransitionSpec = {
            ContentTransform(slideInHorizontally { -it }, slideOutHorizontally { it })
        })
}
