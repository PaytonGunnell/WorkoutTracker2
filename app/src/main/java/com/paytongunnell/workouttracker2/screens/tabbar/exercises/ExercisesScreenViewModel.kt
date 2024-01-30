package com.paytongunnell.workouttracker2.screens.tabbar.exercises

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.paytongunnell.workouttracker2.model.BodyPart
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.ExerciseEquipment
import com.paytongunnell.workouttracker2.model.ExerciseFilter

class ExercisesScreenViewModel(
    val exercises: List<Exercise>
): ViewModel() {

//    private var filteredExercises: SnapShotStateList<Exercise> by rem
    private var _filteredExercises by mutableStateOf(exercises)
    val filteredExercises: List<Exercise>
        get() = _filteredExercises

    private var _exerciseFilter by mutableStateOf(ExerciseFilter())

    var exerciseFilter: ExerciseFilter
        get() = _exerciseFilter
        set(value) {
            _filteredExercises = exercises
            value.bodyPart?.let {  bp ->
                _filteredExercises = _filteredExercises.filter { it.bodyPart == bp }
            }
            value.equipment?.let { eq ->
                _filteredExercises = _filteredExercises.filter { it.equipment == eq }
            }
            _exerciseFilter = value
        }

    fun filterBodyPart(bodyPart: BodyPart) {
        if (bodyPart == exerciseFilter.bodyPart) {
            exerciseFilter = exerciseFilter.copy(bodyPart = null)
        } else {
            exerciseFilter = exerciseFilter.copy(bodyPart = bodyPart)
        }
    }

    fun filterEquipment(equipment: ExerciseEquipment) {
        if (equipment == exerciseFilter.equipment) {
            exerciseFilter = exerciseFilter.copy(equipment = null)
        } else {
            exerciseFilter = exerciseFilter.copy(equipment = equipment)
        }
    }

    fun searchEquipment(searchTerm: String) {
        _filteredExercises = exercises.filter { exercise ->
            val a = _exerciseFilter.bodyPart ?: exercise.bodyPart
            val b = _exerciseFilter.equipment ?: exercise.equipment

            return@filter (a == exercise.bodyPart && b == exercise.equipment && exercise.name.contains(searchTerm))
        }
    }

    class Factory(
        private val exercises: List<Exercise>,
    ) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExercisesScreenViewModel::class.java)) {
                return ExercisesScreenViewModel(exercises) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}