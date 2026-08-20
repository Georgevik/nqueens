package com.georgevik.nqueens.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoreEntity::class], version = 1)
abstract class NQueensDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao
}
