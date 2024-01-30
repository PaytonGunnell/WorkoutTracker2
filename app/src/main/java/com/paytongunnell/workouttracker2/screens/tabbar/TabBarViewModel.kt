package com.paytongunnell.workouttracker2.screens.tabbar

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import com.paytongunnell.workouttracker2.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TabBarViewModel @Inject constructor(
    application: Application,
    private val repository: WorkoutTrackerRepository
): AndroidViewModel(application) {

    private var _exercises = MutableStateFlow<Response<List<Exercise>>>(Response.Loading())
    val exercises = _exercises.asStateFlow()

    private var _gifs by mutableStateOf<Map<String, File?>>(emptyMap()) // Map<exerciseId, File>
    val gifs: Map<String, File?>
        get() = _gifs

    private var _workouts by mutableStateOf<List<Workout>>(emptyList())
    val workouts: List<Workout>
        get() = _workouts

    // ExerciseScreen: exercise info
    private var _exerciseClicked by mutableStateOf<Exercise?>(null)
    val exerciseClicked: Exercise?
        get() = _exerciseClicked

    private var _workoutClicked by mutableStateOf<Workout?>(null)
    val workoutClicked: Workout?
        get() = _workoutClicked

    init {
        viewModelScope.launch {
            repository.getAllLocalExercises()
                .collect {
                    _exercises.value = it
                }
            _gifs = repository.getGifs()
            _workouts = repository.getAllLocalWorkouts()
        }
    }

    fun refreshWorkoutHistory() {
        val a = _workouts.filter { it.exercises.any { it.exerciseId == "" } }
        a.forEach { workout ->
            val b = workout.exercises.filter { it.exerciseId == "" }
            b.forEach { set ->
                val c = set.sets
            }
        }
        viewModelScope.launch {
            _workouts = repository.getAllLocalWorkouts()
        }
    }

//    fun deleteAllHistory() {
//        viewModelScope.launch {
//            repository.deleteAllHistory()
//        }
//    }

    fun createNewExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.createNewExercise(exercise)
            repository.getAllLocalExercises()
                .collect {
                    _exercises.value = it
                }
        }
    }

    fun clickExerciseInfo(exercise: Exercise) {
        _exerciseClicked = exercise
    }
    fun clickOffExerciseInfo() {
        _exerciseClicked = null
    }

    fun clickWorkoutInfo(workout: Workout) {
        _workoutClicked = workout
    }
    fun clickOffWorkoutInfo() {
        _workoutClicked = null
    }
}