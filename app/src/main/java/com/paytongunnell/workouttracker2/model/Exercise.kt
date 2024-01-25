package com.paytongunnell.workouttracker2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/*
    TODO: add pending download and pending deletion for firebase syncing.
        Default exercises will not be synced with firebase.

 */
// Chart: for each workout that contains a given exercise take the best performing set for that exercise and record that set on the chart

@Serializable
@Entity("exercise_table")
data class Exercise(
    @PrimaryKey
    val id: String = "arg without default",
    val userId: String? = null, // used to separate the custom made exercises by the user that created them
    val customMade: Boolean = false,
    val name: String = "arg without default",
    val gifUrl: String? = null,
    val bodyPart: BodyPart? = null,
    val equipment: ExerciseEquipment? = null,
    val target: MuscleGroup? = null,
    val secondaryMuscles: List<String>? = null,
    val instructions: List<String>? = null,
    val timeStampLastClicked: Long? = null,
    val prevLbs: Double? = null,
    val prevReps: Int? = null,
    val newPrevLbs: Double? = null,
    val newPrevReps: Int? = null
//    val history: List<String> = emptyList() // id WorkoutSet
//    val history: List<ExerciseSet> = emptyList()
//    val prevWeight: Double,
//    val prevReps: Double,
//    val prevRpe: Double
)

data class ExerciseFilter(
    val bodyPart: BodyPart? = null,
    val equipment: ExerciseEquipment? = null
)

//@Serializable
//data class ExerciseSet( // tracks sets for a given exercise
//    @PrimaryKey
//    val id: String
//)

