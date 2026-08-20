package com.georgevik.nqueens.ui.screen.game.model

data class GameUi(
    val attempts: Int,
    val markedCells: List<Cell>,
    val startTime: Long,
    val highlightQueensLeft: Boolean,
    val showConflict: Boolean,
    val showVictory: Boolean,
) {
    val queens = markedCells.filter { it.isQueen }

    companion object {
        fun create() = GameUi(
            attempts = 0,
            markedCells = emptyList(),
            startTime = System.currentTimeMillis(),
            highlightQueensLeft = false,
            showConflict = false,
            showVictory = false,
        )
    }
}
