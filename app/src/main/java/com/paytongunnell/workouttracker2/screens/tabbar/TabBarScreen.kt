package com.paytongunnell.workouttracker2.screens.tabbar

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
import androidx.compose.material3.BottomSheetScaffold
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.WorkoutTrackerPopup
import com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.WorkoutTrackerViewModel
import com.paytongunnell.workouttracker2.utils.Response
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarScreen(
    viewModel: TabBarViewModel = hiltViewModel(),
    workoutTrackerViewModel: WorkoutTrackerViewModel = hiltViewModel()
) {

    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    var selectedIndex by remember { mutableStateOf(2) }

    // sheet
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState(rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false))
    var isPartiallyExpanded by remember { mutableStateOf(sheetState.bottomSheetState.currentValue) }


    // Exercise popup toggles
    var showExerciseInfo by remember { mutableStateOf(false) }
    var showNewExercise by remember { mutableStateOf(false) }

    // WorkoutTracker popup toggles
    var showAddExercises by remember { mutableStateOf(false) }
    var showFinishWorkout by remember { mutableStateOf(false) }
    var showRestTimer by remember { mutableStateOf(false) }

    // History popup toggles
    var showWorkoutInfo by remember { mutableStateOf(false) }

    // dim effect
    var dimBackground by remember { mutableStateOf(showRestTimer || showWorkoutInfo || showFinishWorkout || showAddExercises || showExerciseInfo || showNewExercise) }



    when (val state = exercises) {
        is Response.Loading -> {

        }
        is Response.Success -> {
            Box(
                modifier = Modifier
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
                                    modifier = Modifier
                                        .fillMaxSize()
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
                                        LazyColumn {
                                            items(state.data) {
                                                val img = BitmapFactory.decodeFile(
                                                    tabBarViewModel.gifs[it.id]?.absolutePath
                                                        ?: null
                                                ).asImageBitmap()
                                                Text(it.name, color = Color.White)
                                                Image(bitmap = img, contentDescription = null)
//                                  GlideImage(model = tabBarViewModel.gifs[it.id], contentDescription = null, modifier = Modifier.fillMaxSize())
                                            }
                                        }
                                    }

                                    1 -> {
                                        HistoryScreen(
                                            workouts = tabBarViewModel.workouts,
                                            onClickWorkout = {
                                                tabBarViewModel.clickWorkoutInfo(workout = it)
                                                showWorkoutInfo = true
                                            },
                                            onRefreshHistory = { tabBarViewModel.refreshWorkoutHistory()/*tabBarViewModel.deleteAllHistory()*/ }
                                        )
                                    }

                                    2 -> {
                                        StartWorkoutScreen(
                                            exercises = state.data,
                                            gifs = tabBarViewModel.gifs,
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
                                            gifs = tabBarViewModel.gifs,
                                            onClickExerciseInfo = {
                                                tabBarViewModel.clickExerciseInfo(it)
                                                showExerciseInfo = true
                                            },
                                            onCreateNewExercise = { tabBarViewModel.createNewExercise(it) },
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
//                            .alpha(if (isPartiallyExpanded == SheetValue.PartiallyExpanded && sheetState.bottomSheetState.hasExpandedState) 1f else 0f)
                    ) {
                        for (i in 1 until 4) {
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
                    tabBarViewModel.exerciseClicked?.let {
                        ExerciseInfoPopup(
                            exercise = it,
                            gif = tabBarViewModel.gifs[it.id],
                            onClose = { showExerciseInfo = false },
                            onClickOffExerciseInfo = {
                                showExerciseInfo = false
                                tabBarViewModel.clickOffExerciseInfo()
                            },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxHeight(0.9f)
                                .align(Alignment.Center)
                        )
                    }
                }
                if (showNewExercise) {
                    NewExercisePopup(
                        onCreateNewExercise = { tabBarViewModel.createNewExercise(it) },
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
                        gifs = tabBarViewModel.gifs,
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
                                workoutTrackerViewModel.onFinishWorkout()
                                tabBarViewModel.refreshWorkoutHistory()
                                showFinishWorkout = false
                                sheetState.bottomSheetState.hide()
                            }
                            workoutTrackerViewModel.reset()
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
                    tabBarViewModel.workoutClicked?.let { workout ->
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
                        onStopRestTimer = { workoutTrackerViewModel.pauseTimer() },
                        onResetRestTimer = { workoutTrackerViewModel.resetTimer() },
                        isTimerRunning = workoutTrackerViewModel.isTimerRunning,
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