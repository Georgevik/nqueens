package com.georgevik.nqueens.domain.model

import java.time.Instant

data class Score(
    val id: String,
    val playedAt: Instant,
    val nQueens: Int,
    val timeConsumed: Long,
    val attempts: Int,
)
