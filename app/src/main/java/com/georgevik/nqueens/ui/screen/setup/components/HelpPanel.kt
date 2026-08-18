package com.georgevik.nqueens.ui.screen.setup.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.ui.unit.sp
import com.georgevik.nqueens.R
import com.georgevik.nqueens.domain.model.HelpLevel

@Composable
fun HelpPanel(
    selected: HelpLevel,
    onSelected: (HelpLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val levels = HelpLevel.entries

    SettingsPanel(title = stringResource(R.string.help_level_title), modifier = modifier) {
        SingleChoiceSegmentedButtonRow(
            Modifier
                .height(IntrinsicSize.Min)
                .padding(top = 24.dp)
        ) {
            levels.forEachIndexed { index, level ->
                SegmentedButton(
                    modifier = Modifier.fillMaxHeight(),
                    selected = level == selected,
                    onClick = { onSelected(level) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = levels.size,
                        baseShape = PanelShape
                    ),
                    icon = {},
                    label = {
                        Text(
                            text = level.label(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            autoSize = TextAutoSize.StepBased(
                                minFontSize = 10.sp,
                                maxFontSize = 14.sp,
                            ),
                            fontWeight = if (level == selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun HelpLevel.label(): String = stringResource(
    when (this) {
        HelpLevel.NONE -> R.string.help_level_none
        HelpLevel.ERROR_ONLY -> R.string.help_level_errors_only
        HelpLevel.FULL -> R.string.help_level_full
    }
)
