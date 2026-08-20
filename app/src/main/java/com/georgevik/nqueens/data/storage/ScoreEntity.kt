package com.georgevik.nqueens.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playedAt: Long, // epoch millis
    val nQueens: Int,
    val timeConsumedMillis: Long,
    val attempts: Int,
)
