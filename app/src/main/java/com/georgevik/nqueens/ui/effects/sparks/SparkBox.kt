package com.georgevik.nqueens.ui.effects.sparks

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private val SPARK_DRIFT_X: IntRange = -20..20
private val SPARK_DRIFT_UP: IntRange = 10..55
private val SPARK_DURATION: IntRange = 650..1500
private val SPARK_START_DELAY: IntRange = 0..2000
private val SPARK_SIZE: IntRange = 5..15
private val GOLD_COLOR = listOf(
    Color(0xFFFFF3B0),
    Color(0xFFFFD54A),
    Color(0xFFFFC107),
    Color(0xFFFFE9A6),
    Color(0xFFFFFFFF),
)

@Composable
fun SparkBox(modifier: Modifier = Modifier, nSparks: Int) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    Box(modifier = modifier.onSizeChanged { boxSize = it }) {
        if (boxSize.width > 0 && boxSize.height > 0) {
            val sizeDp = with(density) {
                DpSize(boxSize.width.toDp(), boxSize.height.toDp())
            }
            repeat(nSparks) {
                SparkDot(boxSize = sizeDp)
            }
        }
    }
}

fun createSpark(boxSize: DpSize): Spark {
    val startX = Random.nextInt(boxSize.width.value.toInt().coerceAtLeast(1))
    val startY = Random.nextInt(boxSize.height.value.toInt().coerceAtLeast(1))
    return Spark(
        start = DpOffset(startX.dp, startY.dp),
        end = DpOffset(
            (startX + SPARK_DRIFT_X.random()).dp,
            (startY - SPARK_DRIFT_UP.random()).dp,
        ),
        duration = SPARK_DURATION.random(),
        delay = SPARK_START_DELAY.random(),
        size = SPARK_SIZE.random().dp,
        color = GOLD_COLOR.random(),
    )
}
