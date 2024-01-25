package com.paytongunnell.workouttracker2.model

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSet(
    @PrimaryKey
    val id: Int = 1,
    val exerciseId: String = "1",
    val setType: SetType = SetType.NORMAL,
    val weight: Double? = null,
    val reps: Int? = null,
    val rpe: Double? = null,
    val previousLbs: Double? = null,
    val previousReps: Int? = null,
    val timestamp: Long? = null,
    val completed: Boolean = false
)

@Serializable
data class ExerciseBlock(
    val exerciseId: String = "1", // the id of the given exercise
    val exerciseName: String = "Null Name",
//    var sets: List<WorkoutSet>,
    var sets: List<WorkoutSet> = emptyList(),
//    var sets: List<String>, // the id of the sets in this set block for the given exercise
    val isSuperSet: Boolean = false
)