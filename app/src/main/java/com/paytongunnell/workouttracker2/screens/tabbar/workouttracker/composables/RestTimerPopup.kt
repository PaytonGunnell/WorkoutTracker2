package com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.ui.theme.lightBlueButton
import com.paytongunnell.workouttracker2.ui.theme.lighterGray
import com.paytongunnell.workouttracker2.ui.theme.stopRed
import com.paytongunnell.workouttracker2.utils.formatRestTime

@Composable
fun RestTimerPopup(
    onClose: () -> Unit,
    time: Long?,
    onStartRestTimer: () -> Unit,
    onSetRestTimer: (Long) -> Unit,
    onStopRestTimer: () -> Unit,
    onResetRestTimer: () -> Unit,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier
) {
    val durationsInMilliseconds = listOf(
        30000L, // 30 seconds
        60000L, // 1 minute
        120000L, // 2 minutes
        180000L, // 3 minutes
        300000L  // 5 minutes
    )
    var selectedDuration by remember { mutableStateOf(time) }
    var timerIsRunning by remember { mutableStateOf(isTimerRunning) }

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 2.dp)
                        .clickable { onClose() }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
                Text(
                    stringResource(R.string.rest_timer),
                    style = MaterialTheme.typography.titleSmall
                )
                Box(
                    modifier = Modifier
                        .alpha(0f)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 2.dp)
                        .alpha(0f)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }

            Text(
                stringResource(R.string.choose_a_duration_below),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.lighterGray)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                durationsInMilliseconds.forEach {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selectedDuration == it) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 10.dp)
                            .clickable {
                                timerIsRunning = false
                                selectedDuration = it
                                onSetRestTimer(it)
                            }
                    ) {
                        Text(
                            formatRestTime(it),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Text(
                formatRestTime(time ?: selectedDuration ?: 0L),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 70.sp),
                modifier = Modifier.padding(bottom = 10.dp)
            )
            if (!timerIsRunning) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.lightBlueButton.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(vertical = 3.dp)
                        .fillMaxWidth()
                        .clickable {
                            timerIsRunning = true
                            onStartRestTimer()
                        }
                ) {
                    Text(
                        stringResource(R.string.start_timer),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.lightBlueButton),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.lightBlueButton.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(vertical = 3.dp)
                            .weight(1f)
                            .clickable {
                                timerIsRunning = false
                                onStopRestTimer()
                            }
                    ) {
                        Text(
                            stringResource(R.string.pause),
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.lightBlueButton),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.05f))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.stopRed.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(vertical = 3.dp)
                            .weight(1f)
                            .clickable {
                                timerIsRunning = false
                                selectedDuration = durationsInMilliseconds[2]
                                onResetRestTimer()
                            }
                    ) {
                        Text(
                            stringResource(R.string.reset),
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.stopRed),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}