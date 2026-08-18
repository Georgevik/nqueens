package com.georgevik.nqueens.ui.screen.game

import com.georgevik.nqueens.ui.screen.game.model.Queen

data class GameUi(val attempts: Int, val queens: List<Queen>, val startTime: Long)
