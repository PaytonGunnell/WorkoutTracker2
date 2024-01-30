package com.paytongunnell.workouttracker2.screens.tabbar.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import com.paytongunnell.workouttracker2.utils.formatMillisToHoursAndMinutes
import com.paytongunnell.workouttracker2.utils.formatTime1
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    workouts: List<Workout>,
    onClickWorkout: (Workout) -> Unit,
    onRefreshHistory: () -> Unit,
    modifier: Modifier = Modifier
) {

    val grouping = workouts.groupBy {
        val localDate = Instant.ofEpochMilli(it.startTime)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        YearMonth.from(localDate)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Box {}
            Text(
                stringResource(R.string.calendar),
                style = MaterialTheme.typography.blueTextButton
            )
        }

        LazyColumn(modifier = Modifier) {
            item {
                Row(modifier.fillMaxWidth()) {
                    Text(
                        stringResource(R.string.history),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable {
                                onRefreshHistory()
                            }
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                    }
                }
            }

            grouping.forEach {
                item {
                    Text(
                        it.key.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())).uppercase(),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    it.value.forEach {
                        val totalVolume = it.exercises.flatMap { exercise -> exercise.sets.mapNotNull { set -> set.weight } }.sumOf { it ?: 0.0 }

                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .fillMaxWidth()
                                .border(
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable { onClickWorkout(it) }
                        ) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        it.name,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                                Text(
                                    formatTime1(it.startTime),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                )
                                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(painterResource(R.drawable.clock_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            formatMillisToHoursAndMinutes(it.endTime - it.startTime),
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(painterResource(R.drawable.weight_hanging_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            totalVolume.toString(),
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                                        Icon(painterResource(R.drawable.trophy_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            stringResource(R.string.n_a),
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            stringResource(R.string.exercise),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            stringResource(R.string.best_set),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    it.exercises.forEach {
                                        val bestSet = it.sets.maxByOrNull { it.weight?.times((it.reps?.toDouble() ?: 0.0)) ?: 0.0 }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                "${it.sets.count()} x ${it.exerciseName}",
                                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                if (bestSet?.weight != null && bestSet?.reps != null) "${bestSet.weight} lb x ${bestSet.reps}" else stringResource(R.string.n_a),
                                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}