package com.paytongunnell.workouttracker2.screens.tabbar.history.composables

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import com.paytongunnell.workouttracker2.utils.calculateOneRepMax
import com.paytongunnell.workouttracker2.utils.formatMillisToDateTime
import com.paytongunnell.workouttracker2.utils.formatMillisToHoursAndMinutes

@Composable
fun WorkoutInfoPopup(
    workout: Workout,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalVolume = workout.exercises.flatMap { exercise -> exercise.sets.mapNotNull { set -> set.weight } }.sumOf { it ?: 0.0 }

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
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
                    "${workout.name}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Edit",
                    style = MaterialTheme.typography.blueTextButton,
                    modifier = Modifier.alpha(0f)
                )
            }
            Text(
                formatMillisToDateTime(workout.startTime),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(painterResource(R.drawable.clock_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        formatMillisToHoursAndMinutes(workout.endTime - workout.startTime),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(painterResource(R.drawable.weight_hanging_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        totalVolume.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(painterResource(R.drawable.trophy_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "N/A",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                workout.exercises.forEach {  exerciseBlock ->
                    item {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                exerciseBlock.exerciseName,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                "1RM",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    exerciseBlock.sets.forEach {
                        item {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.weight(1f)) {
                                    Text(
                                        it.id.toString(),
                                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                                Box(modifier = Modifier.weight(5f)) {
                                    Text(
                                        if (it?.weight != null && it?.reps != null) "${it.weight} lb x ${it.reps}" else "N/A",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    Text(
                                        if (it?.weight != null && it?.reps != null) calculateOneRepMax(
                                            it.weight,
                                            it.reps
                                        ).toString() else "N/A",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}