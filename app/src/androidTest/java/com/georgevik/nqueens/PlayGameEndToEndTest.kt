package com.georgevik.nqueens

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import com.georgevik.nqueens.domain.model.Position
import com.georgevik.nqueens.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class PlayGameEndToEndTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val solution = listOf(
        Position(1, 0),
        Position(3, 1),
        Position(0, 2),
        Position(2, 3)
    )

    @Test
    fun placing_all_four_queens_shows_victory() {
        // 1. Setup screen: shrink the board to 4 queens.
        composeRule.onNodeWithText("Game setup").assertIsDisplayed()
        composeRule.onNodeWithTag("board_size_slider")
            .performSemanticsAction(SemanticsActions.SetProgress) { setProgress -> setProgress(4f) }

        // 2. Start the game and wait for the board to appear.
        composeRule.onNodeWithText("Start Game").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag(Position(0, 0).toTag()).fetchSemanticsNodes().isNotEmpty()
        }

        // 3. Place the four queens in a valid, non-attacking arrangement and submit.
        solution.forEach { pos -> composeRule.onNodeWithTag(pos.toTag()).performClick() }
        composeRule.onNodeWithText("Validate").performClick()

        // 4. The victory dialog is displayed.
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Victory!").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Victory!").assertIsDisplayed()
    }
}

private fun Position.toTag() = "cell_${col}_${row}"
