package com.georgevik.nqueens.ui.effects.sparks

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.DpSize
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun SparkDot(boxSize: DpSize) {
    var spark by remember { mutableStateOf(createSpark(boxSize)) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(spark) {
        progress.snapTo(0f)
        delay(spark.delay.milliseconds)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = spark.duration),
        )
        spark = createSpark(boxSize)
    }

    val twinkle = sin(progress.value * PI.toFloat())
    val x = spark.start.x + (spark.end.x - spark.start.x) * progress.value
    val y = spark.start.y + (spark.end.y - spark.start.y) * progress.value

    Box(
        Modifier
            .offset(x = x, y = y)
            .size(spark.size)
            .scale(0.33f + 0.67f * twinkle)
            .alpha(twinkle)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(spark.color, spark.color.copy(alpha = 0f)),
                ),
                shape = CircleShape,
            )
    )
}
