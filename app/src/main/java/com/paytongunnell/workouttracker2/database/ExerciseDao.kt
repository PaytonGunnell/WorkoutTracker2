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

    @Query("SELECT * FROM exercise_table ORDER BY name")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise_table WHERE customMade = true ORDER BY name")
    suspend fun getAllCustomMadeExercises(): List<Exercise>

    @Query("SELECT * FROM exercise_table WHERE id = :id LIMIT 1")
    suspend fun getExercise(id: String): Exercise

    @Query("SELECT * FROM exercise_table WHERE id IN (:exerciseIds)")
    suspend fun getExercisesWithIds(exerciseIds: List<String>): List<Exercise>

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAllExercises()

    @Query("DELETE FROM exercise_table WHERE id = :exerciseId")
    suspend fun deleteExerciseWithId(exerciseId: String)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("SELECT COUNT(*) FROM exercise_table WHERE customMade = false")
    suspend fun getCount(): Int

//    @Query(
//        "UPDATE exercise_table SET" +
//                " timeStampLastClicked = :timeStampLastClicked," +
//                " prevLbs = :prevLbs," +
//                " prevReps = :prevReps," +
//                " newPrevLbs = :newPrevLbs," +
//                " newPrevReps = :newPrevReps" +
//                 " WHERE id = :id"
//    )
//    suspend fun updateExerciseById(
//        timeStampLastClicked: Long,
//        prevLbs: Double?,
//        prevReps: Int?,
//        newPrevLbs: Double?,
//        newPrevReps: Int?,
//        id: String
//    )

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}