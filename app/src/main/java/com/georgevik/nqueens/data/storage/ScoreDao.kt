package com.georgevik.nqueens.data.storage

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY playedAt DESC")
    fun scorePaged(): PagingSource<Int, ScoreEntity>
}
