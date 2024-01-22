package com.paytongunnell.workouttracker2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity("exercise_table")
data class Exercise(
    @PrimaryKey
    val id: String,
    val name: String,
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
    val newPrevReps: Int? = null,
    val history: List<String> = emptyList() // id WorkoutSet
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

