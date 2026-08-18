package com.georgevik.nqueens.ui.screen.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.georgevik.nqueens.ui.screen.game.model.Queen

private const val QUEEN_GLYPH = "♛"

@Composable
fun Board(size: Int, queens: List<Queen>, modifier: Modifier = Modifier) {
    val light = MaterialTheme.colorScheme.primaryContainer
    val dark = MaterialTheme.colorScheme.surfaceContainerHighest
    val errorColor = MaterialTheme.colorScheme.errorContainer

    Column(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        repeat(size) { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                repeat(size) { col ->
                    val queen = queens.firstOrNull { it.row == row && it.col == col }
                    val cellColor = when {
                        queen?.showError == true -> errorColor
                        (row + col) % 2 == 1 -> dark
                        else -> light
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(cellColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (queen != null) {
                            Text(
                                text = QUEEN_GLYPH,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = 12.sp,
                                    maxFontSize = 96.sp,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
