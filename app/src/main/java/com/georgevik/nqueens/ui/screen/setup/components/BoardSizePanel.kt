package com.georgevik.nqueens.ui.screen.setup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardSizePanel(
    nQueens: Int,
    onQueenUpdate: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    SettingsPanel(title = stringResource(R.string.board_size_title), modifier = modifier) {
        Text(
            nQueens.toString(),
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            stringResource(R.string.board_size_queens_label),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                range.first.toString(),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.size(8.dp))
            val sliderColors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                activeTickColor = MaterialTheme.colorScheme.primaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                inactiveTickColor = MaterialTheme.colorScheme.primaryContainer
            )
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .testTag("board_size_slider"),
                value = nQueens.toFloat(),
                onValueChange = { onQueenUpdate(it.roundToInt()) },
                colors = sliderColors,
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                            )
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                },
                track = { sliderState ->
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        colors = sliderColors,
                        thumbTrackGapSize = 0.dp,
                    )
                },
                steps = (range.last - range.first - 1),
                valueRange = range.first.toFloat()..range.last.toFloat()
            )
            Spacer(Modifier.size(8.dp))
            Text(
                range.last.toString(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
