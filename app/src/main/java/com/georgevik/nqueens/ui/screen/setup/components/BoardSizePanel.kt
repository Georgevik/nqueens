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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R
import kotlin.math.roundToInt

data class SliderUi(
    val nQueens: Int,
    val minQueens: Int,
    val maxQueens: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardSizePanel(
    modifier: Modifier = Modifier,
    ui: SliderUi = SliderUi(
        nQueens = 8,
        minQueens = 4,
        maxQueens = 12
    ),
    onQueenUpdate: (Int) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    SettingsPanel(title = stringResource(R.string.board_size_title), modifier = modifier) {
        Text(
            "8",
            modifier = Modifier.padding(top = 32.dp),
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            stringResource(R.string.board_size_queens_label),
            style = MaterialTheme.typography.labelLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                ui.minQueens.toString(),
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
                modifier = Modifier.weight(1f),
                value = ui.nQueens.toFloat(),
                onValueChangeFinished = { onQueenUpdate(sliderPosition.roundToInt()) },
                onValueChange = { sliderPosition = it },
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
                steps = (ui.maxQueens - ui.minQueens),
                valueRange = ui.minQueens.toFloat()..ui.maxQueens.toFloat()
            )
            Spacer(Modifier.size(8.dp))
            Text(
                ui.maxQueens.toString(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
