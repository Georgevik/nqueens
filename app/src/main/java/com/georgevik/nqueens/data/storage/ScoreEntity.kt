package com.georgevik.nqueens.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey val id: String,
    val playedAt: Long,
    val nQueens: Int,
    val timeConsumedMillis: Long,
    val attempts: Int,
)
