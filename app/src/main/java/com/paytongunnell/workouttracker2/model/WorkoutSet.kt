package com.paytongunnell.workouttracker2.model

import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSet(
    @PrimaryKey
    val id: Int,
    val exerciseId: String,
    val setType: SetType = SetType.NORMAL,
    val weight: Double? = null,
    val reps: Int? = null,
    val rpe: Double? = null,
    val previousLbs: Double?,
    val previousReps: Int?,
    val timestamp: Long? = null,
    val completed: Boolean = false
)

@Serializable
data class ExerciseBlock(
    val exerciseId: String, // the id of the given exercise
    val exerciseName: String,
//    var sets: List<WorkoutSet>,
    var sets: MutableList<WorkoutSet>,
//    var sets: List<String>, // the id of the sets in this set block for the given exercise
    val isSuperSet: Boolean
)