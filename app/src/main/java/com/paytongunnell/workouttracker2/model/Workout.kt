package com.paytongunnell.workouttracker2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Random
import java.util.UUID

// TODO: add pending download and pending deletion for firebase syncing

/*
    TODO: Data Synchronization
        If data is made without being logged in, and the user logs in the user will be given 3 options
        transfer old data (this will add the data to any data that is already on your profile), or delete old data,
        keep old data but don't transfer it.
 */

@Serializable
@Entity("workout_table")
data class Workout(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "none", // Used to separate the data by users, null if no user is logged in.
    val name: String = "Workout",
    var note: String = "",
    var startTime: Long = System.currentTimeMillis(),
    var endTime: Long = System.currentTimeMillis(),
    var exercises: List<ExerciseBlock> = emptyList()
)

val testWorkout = Workout(
    name = "Midnight Workout",
    note = "this is a note",
    startTime = System.currentTimeMillis() - 1000000,
    endTime = System.currentTimeMillis(),
    exercises = listOf(
        ExerciseBlock(
            exerciseId = "2",
            exerciseName = "Bicep Curl (Machine)",
            sets = mutableListOf(
                WorkoutSet(
                    id = 1, // You may adjust the range or use specific IDs as needed
                    exerciseId = "2", // Generating a random exerciseId using UUID
                    setType = SetType.NORMAL, // You can randomly select a set type or use a specific type
                    weight = 3.0,
                    reps = 3,
                    rpe = if (Random(11999L).nextBoolean()) Random(11999L).nextDouble() else null,
                    previousLbs = if (Random(11999L).nextBoolean()) Random(11999L).nextDouble() else null,
                    previousReps = if (Random(11999L).nextBoolean()) Random(11999L).nextInt() else null,
                    timestamp = if (Random(11999L).nextBoolean()) System.currentTimeMillis() else null,
                    completed = Random(11999L).nextBoolean()
                )
            ),
            isSuperSet = false
        ),
        ExerciseBlock(
            exerciseId = "3",
            exerciseName = "Overhead press",
            sets = mutableListOf(
                WorkoutSet(
                    id = 1, // You may adjust the range or use specific IDs as needed
                    exerciseId = "2", // Generating a random exerciseId using UUID
                    setType = SetType.NORMAL, // You can randomly select a set type or use a specific type
                    weight = 3.0,
                    reps = 3,
                    rpe = 5.0,
                    previousLbs = 50.0,
                    previousReps = 9,
                    timestamp = if (Random(11999L).nextBoolean()) System.currentTimeMillis() else null,
                    completed = true
                ),
                WorkoutSet(
                    id = 1, // You may adjust the range or use specific IDs as needed
                    exerciseId = "2", // Generating a random exerciseId using UUID
                    setType = SetType.NORMAL, // You can randomly select a set type or use a specific type
                    weight = 3.0,
                    reps = 3,
                    rpe = 5.0,
                    previousLbs = 50.0,
                    previousReps = 9,
                    timestamp = if (Random(11999L).nextBoolean()) System.currentTimeMillis() else null,
                    completed = true
                ),
                WorkoutSet(
                    id = 1, // You may adjust the range or use specific IDs as needed
                    exerciseId = "2", // Generating a random exerciseId using UUID
                    setType = SetType.NORMAL, // You can randomly select a set type or use a specific type
                    weight = 3.0,
                    reps = 3,
                    rpe = 5.0,
                    previousLbs = 50.0,
                    previousReps = 9,
                    timestamp = if (Random(11999L).nextBoolean()) System.currentTimeMillis() else null,
                    completed = true
                ),
                WorkoutSet(
                    id = 1, // You may adjust the range or use specific IDs as needed
                    exerciseId = "2", // Generating a random exerciseId using UUID
                    setType = SetType.NORMAL, // You can randomly select a set type or use a specific type
                    weight = 3.0,
                    reps = 3,
                    rpe = 5.0,
                    previousLbs = 50.0,
                    previousReps = 9,
                    timestamp = if (Random(11999L).nextBoolean()) System.currentTimeMillis() else null,
                    completed = true
                )
            ),
            isSuperSet = false
        )
    )
)