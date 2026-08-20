package com.georgevik.nqueens.domain.repository

import androidx.paging.PagingData
import com.georgevik.nqueens.domain.model.Score
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {
    suspend fun insertScore(score: Score)
    fun scores(): Flow<PagingData<Score>>
}
