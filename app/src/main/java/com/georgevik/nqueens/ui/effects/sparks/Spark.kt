package com.georgevik.nqueens.ui.effects.sparks

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

data class Spark(
    val start: DpOffset,
    val end: DpOffset,
    val duration: Int,
    val delay: Int,
    val size: Dp,
    val color: Color,
)
