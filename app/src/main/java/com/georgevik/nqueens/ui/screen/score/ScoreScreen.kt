package com.georgevik.nqueens.ui.screen.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.georgevik.nqueens.R
import com.georgevik.nqueens.ui.screen.score.components.ScoreRow
import com.georgevik.nqueens.ui.screen.score.model.ScoreboardRowUi
import com.georgevik.nqueens.ui.theme.NQueensTheme
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

@Composable
fun ScoreScreen(viewModel: ScoreViewModel = hiltViewModel()) {
    val scores = viewModel.scores.collectAsLazyPagingItems()
    ScoreContent(scores = scores)
}

@Composable
private fun ScoreContent(scores: LazyPagingItems<ScoreboardRowUi>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column {
            Text(
                text = stringResource(R.string.score_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.score_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                count = scores.itemCount,
                key = scores.itemKey { it.id },
            ) { index ->
                scores[index]?.let { row -> ScoreRow(rank = index + 1, row = row) }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScoreScreenPreview() {
    val scores = flowOf(PagingData.from(previewScores)).collectAsLazyPagingItems()
    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ScoreContent(scores = scores)
            }
        }
    }
}

private val previewScores = List(4) { i ->
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "04-08-1999",
        nQueens = 4 + i,
        timeConsumed = "0$i:${i}0:2",
        attempts = i + 1,
    )
}
