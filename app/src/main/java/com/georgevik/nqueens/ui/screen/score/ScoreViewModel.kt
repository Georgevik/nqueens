package com.georgevik.nqueens.ui.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.georgevik.nqueens.domain.model.Score
import com.georgevik.nqueens.domain.repository.ScoreRepository
import com.georgevik.nqueens.infra.extensions.toUiFormat
import com.georgevik.nqueens.ui.screen.score.model.ScoreboardRowUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ScoreViewModel @Inject constructor(
    scoreRepository: ScoreRepository,
) : ViewModel() {

    val scores: Flow<PagingData<ScoreboardRowUi>> by lazy {
        scoreRepository.scores()
            .map { pagingData -> pagingData.map { it.toUi() } }
            .cachedIn(viewModelScope)
    }
}

private fun Score.toUi() = ScoreboardRowUi(
    id = id.toString(),
    date = playedAt.toUiFormat(),
    nQueens = nQueens,
    timeConsumed = timeConsumed.milliseconds.toComponents { hours, minutes, seconds, nanoseconds ->
        val tenths = nanoseconds / 100_000_000L
        "%02d:%02d:%02d:%d".format(hours, minutes, seconds, tenths)
    },
    attempts = attempts,
)
