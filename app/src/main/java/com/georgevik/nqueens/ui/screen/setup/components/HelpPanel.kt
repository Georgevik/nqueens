package com.georgevik.nqueens.ui.screen.setup.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.georgevik.nqueens.R

data class HelpUi(val options: List<String>, val selectedIndex: Int)

@Composable
fun HelpPanel(
    modifier: Modifier = Modifier,
    ui: HelpUi = HelpUi(
        options = listOf(
            stringResource(R.string.help_level_none),
            stringResource(R.string.help_level_errors_only),
            stringResource(R.string.help_level_full),
        ),
        selectedIndex = 0
    ),
    selectedIndex: Int,
    onSelectedIndex: (Int) -> Unit
) {
    SettingsPanel(title = stringResource(R.string.help_level_title), modifier = modifier) {
        SingleChoiceSegmentedButtonRow(
            Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 24.dp)
        ) {
            ui.options.forEachIndexed { index, text ->
                SegmentedButton(
                    modifier = Modifier.fillMaxHeight(),
                    selected = selectedIndex == index,
                    onClick = { onSelectedIndex(index) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = ui.options.size,
                        baseShape = PanelShape
                    ),
                    icon = {},
                    label = {
                        Text(
                            text = text,
                            textAlign = TextAlign.Center,
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}
