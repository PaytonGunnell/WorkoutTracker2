package com.paytongunnell.workouttracker2.screens.tabbar

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paytongunnell.workouttracker2.screens.destinations.AuthScreenDestination
import com.paytongunnell.workouttracker2.screens.destinations.AuthTestScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.ExercisesScreen
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.ExerciseInfoPopup
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.IconTab
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.NewExercisePopup
import com.paytongunnell.workouttracker2.screens.tabbar.history.HistoryScreen
import com.paytongunnell.workouttracker2.screens.tabbar.history.composables.CalendarPopup
import com.paytongunnell.workouttracker2.screens.tabbar.history.composables.WorkoutInfoPopup
import com.paytongunnell.workouttracker2.screens.tabbar.startworkout.StartWorkoutScreen
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.WorkoutTrackerPopup
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.WorkoutTrackerViewModel
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables.AddExercisesPopup
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables.FinishWorkoutPopup
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables.RestTimerPopup
import com.paytongunnell.workouttracker2.utils.Response
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun TabBarScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: TabBarViewModel = hiltViewModel(),
    workoutTrackerViewModel: WorkoutTrackerViewModel = hiltViewModel()
) {

    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    var selectedIndex by remember { mutableStateOf(2) }

    // sheet
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState(rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false))
    var isPartiallyExpanded by mutableStateOf(sheetState.bottomSheetState.currentValue)


    // Exercise popup toggles
    var showExerciseInfo by remember { mutableStateOf(false) }
    var showNewExercise by remember { mutableStateOf(false) }

    // WorkoutTracker popup toggles
    var showAddExercises by remember { mutableStateOf(false) }
    var showFinishWorkout by remember { mutableStateOf(false) }
    var showRestTimer by remember { mutableStateOf(false) }

    // History popup toggles
    var showWorkoutInfo by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }

    // dim effect
    var dimBackground by mutableStateOf(showRestTimer || showWorkoutInfo || showFinishWorkout || showAddExercises || showExerciseInfo || showNewExercise)



    when (val state = exercises) {
        is Response.Loading -> {

        }
        is Response.Success -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Box(modifier = Modifier.weight(1f)) {
                        BottomSheetScaffold(
                            modifier = Modifier
                                .fillMaxWidth(),
                            scaffoldState = sheetState,
                            sheetPeekHeight = 60.dp,
                            sheetDragHandle = {
                                Box(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(Color.Gray)
                                        .height(5.dp)
                                        .width(50.dp)
                                )
                            },
                            sheetContent = {
                                WorkoutTrackerPopup(
                                    exercises = state.data,
                                    workoutTrackerViewModel = workoutTrackerViewModel,
                                    isPartiallyExpanded = isPartiallyExpanded == SheetValue.PartiallyExpanded,
                                    onClickRestTimer = { showRestTimer = true },
                                    onClickFinishWorkout = { showFinishWorkout = true },
                                    onClickAddExercise = { showAddExercises = true },
                                    onClickCancelWorkout = { coroutineScope.launch {
                                        sheetState.bottomSheetState.hide()
                                        workoutTrackerViewModel.reset()
                                    }},
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(0.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                when (selectedIndex) {
                                    0 -> {
                                        // Log Out
                                        Button(onClick = {
                                            viewModel.signOut()
                                            navigator.navigate(AuthScreenDestination)
                                        }) {
                                            Text(if (viewModel.isUserSignedIn()) "Sign Out" else "Sign Up/In")
                                        }
                                    }

                                    1 -> {
                                        HistoryScreen(
                                            workouts = viewModel.workouts,
                                            onClickWorkout = {
                                                viewModel.clickWorkoutInfo(workout = it)
                                                showWorkoutInfo = true
                                            },
                                            onRefreshHistory = { viewModel.refreshWorkoutHistory()/*tabBarViewModel.deleteAllHistory()*/ }
                                        )
                                    }

                                    2 -> {
                                        StartWorkoutScreen(
                                            exercises = state.data,
                                            gifs = viewModel.gifs,
                                            onStartWorkout = {
                                                coroutineScope.launch {
                                                    workoutTrackerViewModel.startTracker()
                                                    sheetState.bottomSheetState.expand()
                                                }
                                            }
                                        )
                                    }
                                    3 -> {
                                        ExercisesScreen(
                                            exercises = state.data,
                                            gifs = viewModel.gifs,
                                            onClickExerciseInfo = {
                                                viewModel.clickExerciseInfo(it)
                                                showExerciseInfo = true
                                            },
                                            onCreateNewExercise = { viewModel.createNewExercise(it) },
                                            onClickNewExercise = { showNewExercise = true },
                                            modifier = Modifier
                                        )
                                    }

                                    4 -> {}
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(top = 3.dp)
                    ) {
                        for (i in 0 until 4) {
                            IconTab(
                                index = i,
                                selectedTab = selectedIndex,
                                onClick = { selectedIndex = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                }

                // Dim Effect
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = if (dimBackground) 0.5f else 0f)))

                if (showExerciseInfo) {
                    viewModel.exerciseClicked?.let {
                        ExerciseInfoPopup(
                            exercise = it,
                            gif = viewModel.gifs[it.id],
                            onClose = { showExerciseInfo = false },
                            onClickOffExerciseInfo = {
                                showExerciseInfo = false
                                viewModel.clickOffExerciseInfo()
                            },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxHeight(0.9f)
                                .align(Alignment.Center)
                        )
                    }
                }
                if (showCalendar) {
                    CalendarPopup(onDismiss = { showCalendar = false })
                }
                if (showNewExercise) {
                    NewExercisePopup(
                        onCreateNewExercise = { viewModel.createNewExercise(it) },
                        onDismiss = { showNewExercise = false },
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxHeight(0.2f)
                            .align(Alignment.Center)
                    )
                }
                if (showAddExercises) {
                    AddExercisesPopup(
                        exercises = state.data,
                        gifs = viewModel.gifs,
                        onAddExercises = { workoutTrackerViewModel.addExercises(it) },
                        onCancel = { showAddExercises = false },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxHeight(0.8f)
                            .align(Alignment.Center)
                    )
                }

                if (showFinishWorkout) {
                    FinishWorkoutPopup(
                        onClickFinish = {
                            coroutineScope.launch {
                                workoutTrackerViewModel.finishWorkout()
                                viewModel.refreshWorkoutHistory()
                                showFinishWorkout = false
                                sheetState.bottomSheetState.hide()
                            }
                        },
                        onClickCancel = {
                            showFinishWorkout = false
                        },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                    )
                }
                if (showWorkoutInfo) {
                    viewModel.workoutClicked?.let { workout ->
                        WorkoutInfoPopup(
                            workout = workout,
                            onClose = { showWorkoutInfo = false },
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .align(Alignment.Center)
                                .fillMaxHeight(0.6f)
                        )
                    }
                }
                if (showRestTimer) {
                    RestTimerPopup(
                        onClose = { showRestTimer = false },
                        time = workoutTrackerViewModel.restTimer?.timerSeconds,
                        onStartRestTimer = {
                            workoutTrackerViewModel.startRestTimer()
                        },
                        onSetRestTimer = {
                            workoutTrackerViewModel.setRestTimer(it)
                        },
                        onStopRestTimer = { workoutTrackerViewModel.pauseRestTimer() },
                        onResetRestTimer = { workoutTrackerViewModel.resetRestTimer() },
                        isTimerRunning = workoutTrackerViewModel.workoutTracker.isRestTimerRunning,
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .align(Alignment.Center)
                            .fillMaxHeight(0.4f)
                    )
                }
            }
        }
        is Response.Error -> {
            Text("${state.message}")
        }
    }
}