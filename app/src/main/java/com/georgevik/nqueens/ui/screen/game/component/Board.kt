package com.georgevik.nqueens.ui.screen.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.georgevik.nqueens.domain.model.Position
import com.georgevik.nqueens.ui.screen.game.model.Cell

private const val QUEEN_GLYPH = "♛"
private const val BLOCKED_GLYPH = "✕"

@Composable
fun Board(
    size: Int,
    markedCells: List<Cell>,
    onCellPress: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    val light = MaterialTheme.colorScheme.primaryContainer
    val dark = MaterialTheme.colorScheme.surfaceContainerHighest
    val errorColor = MaterialTheme.colorScheme.errorContainer
    val crossColor = MaterialTheme.colorScheme.tertiary
    val cellByPosition = remember(markedCells) { markedCells.associateBy { it.position } }

    Column(modifier = modifier.clip(RoundedCornerShape(8.dp))) {
        repeat(size) { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                repeat(size) { col ->
                    val cell = cellByPosition[Position(col, row)]
                    val cellColor = when {
                        cell?.showError == true -> errorColor
                        (row + col) % 2 == 1 -> dark
                        else -> light
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(cellColor)
                            .clickable { onCellPress(Position(col = col, row = row)) },
                        contentAlignment = Alignment.Center,
                    ) {
                        when {
                            cell?.isQueen == true -> CellGlyph(
                                glyph = QUEEN_GLYPH,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            cell?.isAttacked == true -> CellGlyph(
                                glyph = BLOCKED_GLYPH,
                                color = crossColor,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CellGlyph(glyph: String, color: Color) {
    Text(
        text = glyph,
        color = color,
        maxLines = 1,
        autoSize = TextAutoSize.StepBased(
            minFontSize = 12.sp,
            maxFontSize = 96.sp,
        ),
    )
}
