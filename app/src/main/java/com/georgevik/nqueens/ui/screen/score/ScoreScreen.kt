package com.georgevik.nqueens.ui.screen.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R
import com.georgevik.nqueens.ui.screen.score.components.ScoreRow
import com.georgevik.nqueens.ui.screen.score.model.ScoreUi
import com.georgevik.nqueens.ui.screen.score.model.ScoreboardRowUi
import com.georgevik.nqueens.ui.theme.NQueensTheme
import java.util.UUID

@Composable
fun ScoreScreen(ui: ScoreUi = ScoreUi(scoreboard = mockedScores)) {
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
            itemsIndexed(ui.scoreboard, key = { _, row -> row.id }) { index, row ->
                ScoreRow(rank = index + 1, row = row)
            }
        }
    }
}

private val mockedScores = listOf(
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "Aug 09",
        nQueens = 4,
        timeConsumed = "00:18",
        attempts = 1
    ),
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "Aug 17",
        nQueens = 6,
        timeConsumed = "00:52",
        attempts = 2
    ),
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "Aug 18",
        nQueens = 8,
        timeConsumed = "01:24",
        attempts = 3
    ),
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "Aug 05",
        nQueens = 8,
        timeConsumed = "01:47",
        attempts = 4
    ),
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "Aug 15",
        nQueens = 8,
        timeConsumed = "02:10",
        attempts = 5
    ),
    ScoreboardRowUi(
        id = UUID.randomUUID().toString(),
        date = "Aug 12",
        nQueens = 10,
        timeConsumed = "04:38",
        attempts = 7
    ),
)

@Preview(showSystemUi = true)
@Composable
fun ScoreScreenPreview() {
    NQueensTheme {
        Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ScoreScreen()
            }
        }
    }
}
