package com.georgevik.nqueens.ui.screen.game.model

import com.georgevik.nqueens.domain.model.Position

data class Cell(val position: Position, val isQueen: Boolean, val showError: Boolean = false)
