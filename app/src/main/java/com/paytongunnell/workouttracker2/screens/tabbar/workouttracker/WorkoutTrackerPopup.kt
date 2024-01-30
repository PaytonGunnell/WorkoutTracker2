package com.paytongunnell.workouttracker2.screens.tabbar.workouttracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.utils.formatElapsedTime
import com.paytongunnell.workouttracker2.utils.formatRestTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WorkoutTrackerPopup(
    exercises: List<Exercise>,
    isPartiallyExpanded: Boolean,
    workoutTrackerViewModel: WorkoutTrackerViewModel = hiltViewModel(),
    onClickRestTimer: () -> Unit,
    modifier: Modifier = Modifier
) {

    val fadeIn = fadeIn(animationSpec = tween(500))
    val fadeOut = fadeOut(animationSpec = tween(500))

    // Name
    val focusRequester = remember { FocusRequester() }
    var changeName by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Toggle Popups

    Box(modifier.fillMaxSize()) {
        Column(modifier = modifier.padding(horizontal = 10.dp)) {
            Box {
                this@Column.AnimatedVisibility(
                    visible = isPartiallyExpanded,
                    enter = fadeIn,
                    exit = fadeOut
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            workoutTrackerViewModel.workoutTracker.workout.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                        )
                        Text(
                            if (!workoutTrackerViewModel.workoutTracker.isRestTimerRunning)  formatElapsedTime(workoutTrackerViewModel.workoutTracker.elapsedMillis) else "Rest Timer - ${formatRestTime(workoutTrackerViewModel.restTimer?.timerSeconds ?: 0L)}",
                            style = MaterialTheme.typography.labelLarge
                                .copy(color = if (!workoutTrackerViewModel.workoutTracker.isRestTimerRunning) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.lightBlueButton, fontSize = 15.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }

                this@Column.AnimatedVisibility(
                    visible = !isPartiallyExpanded,
                    enter = fadeIn,
                    exit = fadeOut
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 50.dp, top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 17.dp, vertical = 10.dp)
                                .clickable { onClickRestTimer() }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.clock_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
//                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.goGreen,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .clickable { onClickFinishWorkout() }
                        ) {
                            Text(
                                "Finish",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 5.dp),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {

                        BasicTextField(
                            value = workoutTrackerViewModel.workout.name,
//            enabled = changeName,
                            textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                            readOnly = !changeName,
                            onValueChange = {
                                workoutTrackerViewModel.changeName(it)
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    changeName = !changeName
                                }
                            ),
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .onFocusChanged {

                                }
                        )


                        Box(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 5.dp)
                                .clickable {
                                    if (changeName) {
                                        keyboardController?.hide()
                                        changeName = !changeName
                                    } else {
                                        focusRequester.requestFocus()
                                        changeName = !changeName
                                    }
                                }
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = null, modifier = Modifier.rotate(90f))
                        }
                    }
                }

                item {
                    Text(
                        text = formatElapsedTime(workoutTrackerViewModel.elapsedMillis),
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.lighterGray),
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

                item {
                    Box(modifier = Modifier.padding(vertical = 10.dp)) {
                        BasicTextField(
                            value = workoutTrackerViewModel.workout.note, onValueChange = {
                                workoutTrackerViewModel.changeNote(it)
                            },
                            textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                        )

                        if (workoutTrackerViewModel.workout.note == "" || workoutTrackerViewModel.workout.note == null) {
                            Text(
                                text = "Notes",
                                style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary)
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)) {
                        workoutTrackerViewModel.workout.exercises.forEachIndexed { blockIndex, exerciseBlock ->
                            ExerciseBlockView(
                                exerciseBlock = exerciseBlock,
                                onChangeSetWeight = { index, weight ->
                                    workoutTrackerViewModel.changeSetWeight(blockIndex, index, weight)
                                },
                                onChangeSetRepCount = { index, repCount ->
                                    workoutTrackerViewModel.changeSetRepCount(blockIndex, index, repCount)
                                },
                                onToggleCompleteSet = { index, completed ->
                                    workoutTrackerViewModel.changeSetCompleted(blockIndex, index, completed)
                                },
                                onAddSet = {
                                    workoutTrackerViewModel.addSet(blockIndex, it)
                                }
                            )
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .background(
                                color = MaterialTheme.colorScheme.lightBlueButton.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .fillMaxWidth()
                            .padding(5.dp)
                            .clickable { onClickAddExercise() }
                    ) {
                        Text(
                            "Add Exercises",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.lightBlueButton)
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 30.dp)
                            .background(
                                color = MaterialTheme.colorScheme.stopRed.copy(alpha = 0.3f),
                                shape = MaterialTheme.shapes.small
                            )
                            .fillMaxWidth()
                            .padding(5.dp)
                            .clickable { onClickCancelWorkout() }
                    ) {
                        Text(
                            "Cancel Workout",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.stopRed)
                        )
                    }
                }
            }
        }


    }
}