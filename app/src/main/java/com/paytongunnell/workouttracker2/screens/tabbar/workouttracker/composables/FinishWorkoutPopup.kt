package com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.ui.theme.goGreen

@Composable
fun FinishWorkoutPopup(
    onClickFinish: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Text(
                stringResource(R.string.finish_workout),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)) {
                Text(
                    stringResource(R.string.all_invalid_or_empty_sets_will_be_removed),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
                    textAlign = TextAlign.Center
                )
                Text(
                    stringResource(R.string.all_valid_sets_will_be_marked_as_complete),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary),
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 10.dp)
                        .padding(5.dp)
                        .weight(1f)
                        .clickable { onClickCancel() }
                ) {
                    Text(
                        stringResource(R.string.cancel),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.goGreen,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 10.dp)
                        .padding(5.dp)
                        .weight(1f)
                        .clickable { onClickFinish() }
                ) {
                    Text(
                        stringResource(R.string.finish),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}