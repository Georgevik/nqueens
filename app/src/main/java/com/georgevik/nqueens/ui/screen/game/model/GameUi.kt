package com.georgevik.nqueens.ui.screen.game.model

data class GameUi(
    val attempts: Int,
    val markedCells: List<Cell>,
    val startTime: Long
) {
    val queens = markedCells.filter { it.isQueen }
}
