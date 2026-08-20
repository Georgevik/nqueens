package com.georgevik.nqueens.ui.screen.setup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.georgevik.nqueens.domain.model.HelpLevel
import com.georgevik.nqueens.ui.navigation.model.GameConfig
import com.georgevik.nqueens.ui.theme.NQueensTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WinGameTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun game_screen_is_launched_with_the_expected_config() {
        var config: GameConfig? = null
        composeRule.setContent {
            NQueensTheme {
                SetupScreen(onStart = { config = it }, onScore = {})
            }
        }
        composeRule.onNodeWithText("Game setup").assertIsDisplayed()
        composeRule.onNodeWithText("Start Game").performClick()

        assertEquals(GameConfig(nQueens = 8, helpLevel = HelpLevel.NONE), config)
    }
}
