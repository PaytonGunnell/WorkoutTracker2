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

    @Upsert
    suspend fun upsertWorkouts(workouts: List<Workout>)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("DELETE FROM workout_table")
    suspend fun deleteAllWorkouts()

    @Query("SELECT * FROM workout_table WHERE userId = :uId ORDER BY startTime")
    suspend fun getAllWorkouts(uId: String?): List<Workout>

    @Query("SELECT * FROM workout_table WHERE userId = :uId AND id IN (:workoutIds)")
    suspend fun getWorkoutsWithIds(uId: String?, workoutIds: List<String>): List<Workout>

    @Query("DELETE FROM workout_table WHERE id = :workoutId")
    suspend fun deleteWorkoutWithId(workoutId: String)
}