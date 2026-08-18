package com.georgevik.nqueens.ui.screen.score.model

data class ScoreboardRowUi(
    val id: String,
    val date: String,
    val nQueens: Int,
    val timeConsumed: String,
    val attempts: Int
)
