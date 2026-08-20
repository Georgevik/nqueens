package com.georgevik.nqueens.ui.screen.game.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameControls(
    startTime: Long,
    attempts: Int,
    queensLeft: Int,
    highlightQueensLeft: Boolean,
    onReset: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TimerCounter(
                startTime = startTime,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
            CardPanel(
                label = stringResource(R.string.game_queens_left_label),
                value = queensLeft.toString(),
                highlight = highlightQueensLeft,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
            CardPanel(
                label = stringResource(R.string.game_attempts_label),
                value = attempts.toString(),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.game_reset))
            }
            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.game_validate))
            }
        }
    }
}

@Composable
fun CardPanel(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
) {
    val transition = updateTransition(targetState = highlight, label = "cardHighlight")
    val scale by transition.animateFloat(
        label = "textScale",
        transitionSpec = { tween(durationMillis = 250) },
    ) { on -> if (on) 2f else 1f }

    val valueColor by transition.animateColor(
        label = "textColor",
        transitionSpec = { tween(durationMillis = 250) },
    ) { on -> if (on) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Text(
            text = value,
            color = valueColor,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.scale(scale),
        )
    }
}


@Composable
fun TimerCounter(startTime: Long, modifier: Modifier = Modifier) {
    var elapsedMillis by remember(startTime) { mutableLongStateOf(0L) }
    LaunchedEffect(startTime) {
        while (true) {
            elapsedMillis = System.currentTimeMillis() - startTime
            delay(100.milliseconds)
        }
    }

    CardPanel(
        label = stringResource(R.string.game_time_label),
        value = elapsedMillis.toFormatTime(),
        modifier = modifier,
    )
}

private fun Long.toFormatTime(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val tenths = this % 1000 / 100
    return "%02d:%02d:%01d".format(minutes, seconds, tenths)
}
