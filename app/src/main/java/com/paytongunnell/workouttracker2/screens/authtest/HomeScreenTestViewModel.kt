package com.paytongunnell.workouttracker2.screens.authtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.ExerciseBlock
import com.paytongunnell.workouttracker2.model.SetType
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.model.WorkoutSet
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import com.paytongunnell.workouttracker2.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeScreenTestViewModel @Inject constructor(
    application: Application,
    private val repository: WorkoutTrackerRepository
): AndroidViewModel(application) {

    val user = repository.getFirebaseUser()

    private var _exercises = MutableStateFlow<Response<List<Exercise>>?>(null)
    val exercises = _exercises.asStateFlow()

    private var _localExercises = MutableStateFlow<Response<List<Exercise>>?>(null)
    val localExercises = _exercises.asStateFlow()

    private var _workouts = MutableStateFlow<Response<List<Workout>>?>(null)
    val workouts = _workouts.asStateFlow()


    init {
        user?.let {
            viewModelScope.launch {
                repository.syncLocalAndFirebaseDataIfFirstTimeSigningIn(it.uid)
                getLocalExercises()
                getExercisesFromFirebase()
            }
        }
    }


    // TEMP
    fun createNewExercise() {
        val testExercise = Exercise(
            id = UUID.randomUUID().toString(),
            customMade = true,
            userId = user?.uid,
            name = "Test Exercise"
        )
        viewModelScope.launch {
            repository.createNewExercise(user?.uid, testExercise)
            getLocalExercises()
            getExercisesFromFirebase()
        }
    }

    fun getLocalExercises() {
        viewModelScope.launch {
            repository.getAllCustomMadeExercises()
                .collect {
                    _localExercises.value = it
                }
        }
    }

    fun getWorkoutsFromFirebase() {
        user?.let { fbUser ->
            viewModelScope.launch {
                repository.getAllFirebaseWorkouts(fbUser.uid)
                    .collect {
                        _workouts.value = it
                    }
            }
        }
    }

    fun createNewFirebsaeWorkout() {
        user?.let { fbUser ->
            val testWorkout = Workout(
                userId = fbUser.uid,
                name = "Test Workout",
                startTime = 1L,
                endTime = 2L,
                exercises = listOf(
                    ExerciseBlock(
                        exerciseId = "2",
                        exerciseName = "Test Workout Exercise",
                        sets = listOf(
                            WorkoutSet(
                                id = 1,
                                exerciseId = "2",
                                setType = SetType.FAILURE,
                                previousLbs = 2.0,
                                previousReps = 3
                            )
                        ),
                        isSuperSet = false
                    )
                )
            )

            viewModelScope.launch {
                repository.createFirebaseWorkout(fbUser.uid, testWorkout)
            }
        }
    }

    fun getExercisesFromFirebase() {
        user?.let { fbUser ->
            viewModelScope.launch {
                repository.getAllFirebaseExercises(fbUser.uid)
                    .collect {
                        _exercises.value = it
                    }
            }
        }
    }

    fun createNewFirebaseExercise() {
        user?.let { fbUser ->
            val testExercise = Exercise(
                id = UUID.randomUUID().toString(),
                customMade = true,
                userId = fbUser.uid,
                name = "Test Exercise"
            )
            viewModelScope.launch {
                repository.createFirebaseExercise(fbUser.uid, testExercise)
            }
        }
    }
    // TEMP

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}