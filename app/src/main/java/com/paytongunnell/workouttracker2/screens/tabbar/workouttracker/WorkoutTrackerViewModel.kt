package com.paytongunnell.workouttracker2.screens.tabbar.workouttracker

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.ExerciseBlock
import com.paytongunnell.workouttracker2.model.WorkoutSet
import com.paytongunnell.workouttracker2.model.WorkoutTracker
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import com.paytongunnell.workouttracker2.utils.CountDownTimer
import com.paytongunnell.workouttracker2.utils.StopwatchTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class WorkoutTrackerViewModel @Inject constructor(
    private val repository: WorkoutTrackerRepository,
): ViewModel() {

    private val uId = repository.getFirebaseUser()?.uid

    private var _workoutTracker by mutableStateOf(WorkoutTracker(uId = uId))
    val workoutTracker: WorkoutTracker
        get() = _workoutTracker

    val stopwatch = StopwatchTimer()
    var restTimer: CountDownTimer? = null

    init {
        restTimer = CountDownTimer(count = 120000L)

        stopwatch.onTick = {
            _workoutTracker = _workoutTracker.copy(elapsedMillis = it)
            _workoutTracker = _workoutTracker.copy(
                workout = _workoutTracker.workout.copy(
                    endTime = _workoutTracker.workout.startTime + it
                )
            )
        }
    }

    fun startNewWorkout() {
        _workoutTracker = WorkoutTracker(uId = uId)
    }

    // Rest Timer
    fun startRestTimer() {
        restTimer?.start()
        _workoutTracker = _workoutTracker.copy(isRestTimerRunning = true)
    }
    fun setRestTimer(time: Long) {
        restTimer = CountDownTimer(time)
        _workoutTracker = _workoutTracker.copy(isRestTimerRunning = false)
    }
    fun pauseRestTimer() {
        restTimer?.let {
            restTimer = CountDownTimer(count = it.timerCount, originalCount = it.originalTimerCount)
            _workoutTracker = _workoutTracker.copy(isRestTimerRunning = true)
        }
    }
    fun resetRestTimer() {
        restTimer = CountDownTimer(count = 120000L)
        _workoutTracker = _workoutTracker.copy(isRestTimerRunning = false)
    }
    fun getRestTimerSeconds(): Long {
        return restTimer?.timerSeconds ?: 0L
    }

    // Edit Workout
    fun addExerciseBlock(exerciseBlock: ExerciseBlock) {
        val workout = _workoutTracker.workout
        val exercises = workout.exercises
        _workoutTracker = _workoutTracker.copy(workout = workout.copy(exercises = (exercises + exerciseBlock)))
    }
    fun changeName(name: String) {
        val workout = _workoutTracker.workout
        _workoutTracker = _workoutTracker.copy(workout = workout.copy(name = name))
    }
    fun changeNote(note: String) {
        val workout = _workoutTracker.workout
        _workoutTracker = _workoutTracker.copy(workout = workout.copy(note = note))
    }
    fun addExercise(exercise: Exercise) {
        val exerciseBlock = ExerciseBlock(
            exerciseId = exercise.id,
            exerciseName = exercise.name,
            sets = listOf(WorkoutSet(
                id = 1,
                exerciseId = exercise.id,
                previousLbs = exercise.prevLbs,
                previousReps = exercise.prevReps
            )),
            isSuperSet = false
        )

        _workoutTracker = _workoutTracker.copy(
            workout = _workoutTracker.workout.copy(
                exercises = _workoutTracker.workout.exercises.plus(exerciseBlock)
            )
        )
    }
    fun addExercises(exercises: List<Exercise>) {
        Log.d("workouttrackerviewmodel", "addExercises: ${exercises}")
        val exerciseBlocks = exercises.map { exercise ->
            ExerciseBlock(
                exerciseId = exercise.id,
                exerciseName = exercise.name,
                sets = listOf(WorkoutSet(
                    id = 1,
                    exerciseId = exercise.id,
                    previousLbs = exercise.prevLbs,
                    previousReps = exercise.prevReps
                )),
                isSuperSet = false
            )
        }

        _workoutTracker = _workoutTracker.copy(
            workout = _workoutTracker.workout.copy(
                exercises = _workoutTracker.workout.exercises.plus(exerciseBlocks)
            )
        )
    }
    fun addSet(blockIndex: Int, workoutSet: WorkoutSet) {
        _workoutTracker = _workoutTracker.copy(
            workout = _workoutTracker.workout.copy(
                exercises = _workoutTracker.workout.exercises.mapIndexed { i, exercise ->
                    if (i == blockIndex) {
                        exercise.copy(
                            sets = exercise.sets.plus(workoutSet)
                        )
                    } else {
                        exercise
                    }
                }
            )
        )
    }
    fun changeSetWeight(blockIndex: Int, setIndex: Int, weight: Double) {
        _workoutTracker = _workoutTracker.copy(
            workout = _workoutTracker.workout.copy(
                exercises = _workoutTracker.workout.exercises.mapIndexed { i, exercise ->
                    if (i == blockIndex) {
                        exercise.copy(
                            sets = exercise.sets.mapIndexed { j, set ->
                                if (j == setIndex) set.copy(weight = weight)
                                else set
                            }
                        )
                    } else {
                        exercise
                    }
                }
            )
        )
    }
    fun changeSetRepCount(blockIndex: Int, setIndex: Int, repCount: Int) {
        _workoutTracker = _workoutTracker.copy(
            workout = _workoutTracker.workout.copy(
                exercises = _workoutTracker.workout.exercises.mapIndexed { i, exercise ->
                    if (i == blockIndex) {
                        exercise.copy(
                            sets = exercise.sets.mapIndexed { j, set ->
                                if (j == setIndex) set.copy(reps = repCount)
                                else set
                            }
                        )
                    } else {
                        exercise
                    }
                }
            )
        )
    }
    fun changeSetCompleted(blockIndex: Int, setIndex: Int, completed: Boolean) {
        _workoutTracker = _workoutTracker.copy(
            workout = _workoutTracker.workout.copy(
                exercises = _workoutTracker.workout.exercises.mapIndexed { i, exercise ->
                    if (i == blockIndex) {
                        exercise.copy(
                            sets = exercise.sets.mapIndexed { j, set ->
                                if (j == setIndex) set.copy(completed = completed)
                                else set
                            }
                        )
                    } else {
                        exercise
                    }
                }
            )
        )
    }
    fun changeStartTime(time: Long) {
        _workoutTracker = _workoutTracker.copy(workout = _workoutTracker.workout.copy(startTime = time))
    }
    fun changeEndTime(time: Long) {
        _workoutTracker = _workoutTracker.copy(workout = _workoutTracker.workout.copy(endTime = time))
    }
    fun changeAutomaticTiming() {
        _workoutTracker = _workoutTracker.copy(automaticTiming = !_workoutTracker.automaticTiming)
    }
    fun changeStopWatchStartTime(time: Long) {
        stopwatch.changeStartTime(time)
    }
    //

    fun finishWorkout() {
        // save workout
        viewModelScope.launch {
            repository.createNewWorkout(_workoutTracker.workout)
            reset()
        }
    }

    fun reset() {
        _workoutTracker = WorkoutTracker(uId = uId)
        stopwatch.stop()
        restTimer = null
    }

    fun startTracker() {
        viewModelScope.launch {
            delay(1)
            stopwatch.start()
        }
    }
}