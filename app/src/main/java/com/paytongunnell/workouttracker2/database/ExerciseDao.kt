package com.paytongunnell.workouttracker2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.paytongunnell.workouttracker2.model.Exercise

@Dao
interface ExerciseDao {

    @Upsert
    suspend fun upsert(exercise: Exercise)

    @Upsert
    suspend fun upsertExercises(exercises: List<Exercise>)

    @Query("SELECT * FROM exercise_table WHERE customMade = false OR userId = :uId ORDER BY name")
    suspend fun getAllExercises(uId: String): List<Exercise>

    @Query("SELECT * FROM exercise_table WHERE customMade = false OR userId = :uId AND id = :id LIMIT 1")
    suspend fun getExercise(uId: String?, id: String): Exercise

    @Query("SELECT * FROM exercise_table WHERE customMade = false OR userId = :uId AND id IN (:exerciseIds)")
    suspend fun getExercisesWithIds(uId: String, exerciseIds: List<String>): List<Exercise>

    @Query("SELECT * FROM exercise_table WHERE customMade = true AND userId = :uId")
    suspend fun getAllCustomMadeExercises(uId: String?): List<Exercise>

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAllExercises()

    @Query("DELETE FROM exercise_table WHERE id = :exerciseId")
    suspend fun deleteExerciseWithId(exerciseId: String)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("SELECT COUNT(*) FROM exercise_table WHERE customMade = false")
    suspend fun getCount(): Int

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}