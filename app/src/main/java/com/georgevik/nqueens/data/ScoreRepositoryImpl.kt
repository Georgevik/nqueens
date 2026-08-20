package com.georgevik.nqueens.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.georgevik.nqueens.data.storage.ScoreDao
import com.georgevik.nqueens.data.storage.ScoreEntity
import com.georgevik.nqueens.domain.model.Score
import com.georgevik.nqueens.domain.repository.ScoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class ScoreRepositoryImpl @Inject constructor(
    private val scoreDao: ScoreDao,
) : ScoreRepository {

    override suspend fun insertScore(score: Score) {
        scoreDao.insert(score.toEntity())
    }

    override fun scores(): Flow<PagingData<Score>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { scoreDao.scorePaged() },
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    private companion object {
        const val PAGE_SIZE = 20
    }
}

private fun Score.toEntity() = ScoreEntity(
    id = id,
    playedAt = playedAt.toEpochMilli(),
    nQueens = nQueens,
    timeConsumedMillis = timeConsumed,
    attempts = attempts,
)

private fun ScoreEntity.toDomain() = Score(
    id = id,
    playedAt = Instant.ofEpochMilli(playedAt),
    nQueens = nQueens,
    timeConsumed = timeConsumedMillis,
    attempts = attempts,
)
