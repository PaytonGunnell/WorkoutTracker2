package com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.ExerciseBlock
import com.paytongunnell.workouttracker2.model.SetType
import com.paytongunnell.workouttracker2.model.WorkoutSet
import com.paytongunnell.workouttracker2.ui.theme.goGreen
import com.paytongunnell.workouttracker2.ui.theme.lightBlueButton
import com.paytongunnell.workouttracker2.ui.theme.stopRed

@SuppressLint("UnrememberedMutableState")
@Composable
fun ExerciseBlockView(
    exerciseBlock: ExerciseBlock,
    onChangeSetWeight: (Int, Double) -> Unit,
    onChangeSetRepCount: (Int, Int) -> Unit,
    onAddSet: (WorkoutSet) -> Unit,
    onToggleCompleteSet: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(exerciseBlock) }
    var showEditPopup by remember { mutableStateOf(false) }

    Box(modifier = modifier
        .fillMaxSize()
        .clickable { showEditPopup = false }) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.exerciseName,
                    style = MaterialTheme.typography.titleSmall.copy(MaterialTheme.colorScheme.lightBlueButton)
                )
                Row {
                    Box(modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.lightBlueButton.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                        .clickable { /*showEditPopup = true*/ }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.lightBlueButton,
                            modifier = Modifier.rotate(90f)
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.set),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
                Box(modifier = Modifier.weight(3f)) {
                    Text(
                        text = stringResource(R.string.previous),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(modifier = Modifier.weight(2f)) {
                    Text(
                        text = stringResource(R.string.lbs),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(modifier = Modifier.weight(2f)) {
                    Text(
                        text = stringResource(R.string.reps),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                state.sets.forEach { set ->
                    var setCompleted by remember { mutableStateOf(set.completed) }
                    var setWeight by remember { mutableStateOf((set.weight ?: "").toString()) }
                    var repCount by remember { mutableStateOf((set.reps ?: "").toString()) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp)
                            .background(MaterialTheme.colorScheme.goGreen.copy(alpha = if (setCompleted) 0.2f else 0f))
                            .padding(vertical = 10.dp)
                    ) {
                        // SET
                        Box(modifier = Modifier.weight(1f)) {
                            ExerciseBlockButton(
                                onClick = { /*TODO*/ },
                                content = {
                                    if (set.setType == SetType.NORMAL) {
                                        Text(
                                            text = set.id.toString(),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                },
                                modifier = Modifier
                            )
                        }

                        // PREVIOUS
                        if (set.previousLbs != null && set.previousReps != null) {
                            Text(
                                text = "${set.previousLbs} lb x ${set.previousReps}",
                                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        } else {
                            Box(modifier = Modifier.weight(3f)) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .height(5.dp)
                                        .fillMaxWidth(0.3f)
                                        .padding(horizontal = 5.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        // WEIGHT
                        ExerciseBlockButton(
                            onClick = { /*TODO*/ },
                            content = {

                                Box(modifier = Modifier.fillMaxSize()) {
                                    BasicTextField(
                                        value = setWeight, onValueChange = {
                                            setWeight = it
                                            onChangeSetWeight(set.id - 1, it.toDouble())
                                        },
                                        textStyle = MaterialTheme.typography.titleSmall.copy(
                                            MaterialTheme.colorScheme.onBackground
                                        ),
                                        modifier = Modifier.align(Alignment.Center),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        singleLine = true
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .weight(2f)
                        )

                        // REPS
                        ExerciseBlockButton(
                            onClick = { /*TODO*/ },
                            content = {

                                Box(modifier = Modifier.fillMaxSize()) {
                                    BasicTextField(
                                        value = repCount, onValueChange = {
                                            repCount = it
                                            onChangeSetRepCount(set.id - 1, it.toInt())
                                        },
                                        textStyle = MaterialTheme.typography.titleSmall.copy(
                                            MaterialTheme.colorScheme.onBackground
                                        ),
                                        modifier = Modifier.align(Alignment.Center),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        singleLine = true
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)
                                .weight(2f)
                        )

                        // COMPLETED

                        Box(modifier = modifier
                            .weight(1f)
                            .background(
                                if (setCompleted) MaterialTheme.colorScheme.goGreen else MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 10.dp)
                            .clickable {
                                if (setWeight != "" && repCount != "") {
                                    setCompleted = !setCompleted
                                    onToggleCompleteSet(set.id - 1, setCompleted)
                                }
                            }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Text("1", modifier = Modifier.alpha(0f))
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier
                .padding(vertical = 5.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .fillMaxWidth()
                .clickable {
                    val newSet = WorkoutSet(
                        id = state.sets.count() + 1,
                        exerciseId = state.exerciseId,
                        previousLbs = state.sets.last().previousLbs,
                        previousReps = state.sets.last().previousReps
                    )
                    state = state.copy(sets = (state.sets + newSet).toMutableList())
                    onAddSet(newSet)
                }
            ) {
                Text(
                    stringResource(R.string.add_set),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (showEditPopup) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.stopRed,
                            shape = MaterialTheme.shapes.small
                        )
                )
            }
        }
    }
}

@Composable
fun ExerciseBlockButton(
    onClick: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .background(
            MaterialTheme.colorScheme.secondary,
            shape = MaterialTheme.shapes.small
        )
        .padding(horizontal = 10.dp)
        .clickable { onClick() }
    ) {
        content(
            modifier
                .fillMaxSize()
                .align(Alignment.Center))
    }
}