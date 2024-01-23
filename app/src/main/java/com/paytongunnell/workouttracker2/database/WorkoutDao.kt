package com.paytongunnell.workouttracker2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.Workout

@Dao
interface WorkoutDao {
    @Upsert
    suspend fun upsert(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workout_table")
    suspend fun deleteAllWorkouts()

    @Query("SELECT * FROM workout_table ORDER BY startTime")
    suspend fun getAllWorkouts(): List<Workout>
}