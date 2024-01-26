package com.paytongunnell.workouttracker2.network

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.Workout
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object WorkoutTrackerServerService {

    private val database = Firebase.database.reference
    private val storage = Firebase.storage.reference

    /// Realtime Database
    // Add
    suspend fun addExercise(uId: String, exercise: Exercise) {
        database.child("users").child(uId).child("exercises").child(exercise.id).setValue(exercise)
    }

    suspend fun addExercises(uId: String, exercises: List<Exercise>) {
        val path = database.child("users").child(uId).child("exercises")

        exercises.forEach { exercise ->
            path.child(exercise.id).setValue(exercise)
        }
    }

    suspend fun addWorkout(uId: String, workout: Workout) {
        database.child("users").child(uId).child("workouts").child(workout.id).setValue(workout)
    }

    suspend fun addWorkouts(uId: String, workouts: List<Workout>) {
        val path = database.child("users").child(uId).child("workouts")

        workouts.forEach { workout ->
            path.child(workout.id).setValue(workout)
        }
    }

    // Get
    suspend fun getWorkouts(uId: String): List<Workout>? = suspendCancellableCoroutine { continuation ->
        try {
            val query = database.child("users").child(uId).child("workouts")
            query
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.getValue<Map<String, Workout>>()?.values?.toList()
                        continuation.resume(data)
                    } else {
                        val e = task.exception
                        if (e != null) {
                            continuation.resumeWithException(e)
                        } else {
                            continuation.resumeWithException(RuntimeException("Unknown error during getWorkouts"))
                        }
                    }
                }
        } catch(e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    suspend fun getExercise(uId: String, exerciseId: String): Exercise? = suspendCancellableCoroutine { continuation ->
        try {
            database.child("users").child(uId).child("exercises").child(exerciseId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.getValue<Exercise>()
                        continuation.resume(data)
                    } else {
                        val e = task.exception
                        if (e != null) {
                            continuation.resumeWithException(e)
                        } else {
                            continuation.resumeWithException(RuntimeException("Unknown error during getWorkouts"))
                        }
                    }
                }
        } catch(e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    suspend fun getExercises(uId: String): List<Exercise>? = suspendCancellableCoroutine { continuation ->
        try {
            val query = database.child("users").child(uId).child("exercises")
            query
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.getValue<Map<String, Exercise>>()?.values?.toList()
                        continuation.resume(data)
                    } else {
                        val e = task.exception
                        if (e != null) {
                            continuation.resumeWithException(e)
                        } else {
                            continuation.resumeWithException(RuntimeException("Unknown error during getWorkouts"))
                        }
                    }
                }
        } catch(e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    // Delete
    suspend fun deleteExercise(uId: String, exerciseId: String) {
        database.child("users").child(uId).child("exercises").child(exerciseId).removeValue()
    }
    suspend fun deleteExercises(uId: String, exerciseIds: List<String>) {
        val path = database.child("users").child(uId).child("exercises")

        exerciseIds.forEach { id ->
            path.child(id).removeValue()
        }
    }

    suspend fun deleteWorkout(uId: String, workoutId: String) {
        database.child("users").child(uId).child("exercises").child(workoutId).removeValue()
    }
    suspend fun deleteWorkouts(uId: String, workoutIds: List<String>) {
        val path = database.child("users").child(uId).child("workouts")

        workoutIds.forEach { id ->
            path.child(id).removeValue()
        }
    }

    /// Cloud Storage
    fun saveImage(uId: String, path: String, bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        var uploadTask = storage.child(uId).child("profile").putBytes(data)
        uploadTask.addOnFailureListener {
            Log.d("saveImage", "fail")
        }.addOnSuccessListener {
            Log.d("saveImage", "success: images/$path")
        }
    }
    suspend fun getProfileImage(uId: String) {

    }
}