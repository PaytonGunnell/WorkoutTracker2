package com.paytongunnell.workouttracker2.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.getSystemService
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.model.WorkoutSet
import com.paytongunnell.workouttracker2.model.testWorkout
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.network.WorkoutTrackerServerService
import com.paytongunnell.workouttracker2.utils.Response
import com.paytongunnell.workouttracker2.utils.isNetworkConnected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

class WorkoutTrackerRepository(
    private val application: Application,
    private val networkService: ExerciseDBService,
    private val authClient: FirebaseAuthClient,
    private val workoutTrackerServerService: WorkoutTrackerServerService,
    private val
    database: ExerciseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // Stores the id's of all exercises or workouts deleted/created while offline so that when the app is back online the
    // firebase data will be synced with the local data
    private val pendingDeletion = application.getSharedPreferences("Pending_Deletion", Context.MODE_PRIVATE)
    private val pendingUpload = application.getSharedPreferences("Pending_Upload", Context.MODE_PRIVATE)

    private val eventFlagsSharedPreferences = application.getSharedPreferences("Event_Flags", Context.MODE_PRIVATE)

    // Firebase

    // Will get the firebase data associated with the user if it is their first time signing in on the device
    // TODO: sync Workouts
    suspend fun syncLocalAndFirebaseDataIfFirstTimeSigningIn(uId: String) {
        val usersThatHaveLoggedIn = eventFlagsSharedPreferences.getStringSet("userHasLoggedIn", emptySet())
        if (usersThatHaveLoggedIn?.contains(uId) != true) {
            try {
                val firebaseExercises = workoutTrackerServerService.getExercises(uId)
                val localDatabaseExercises = database.exerciseDao.getAllExercises()

                try {
                    database.exerciseDao.upsertExercises(firebaseExercises)
                } catch(e: Exception) {
                    // make firebase exercises pending upload
                    throw e
                }

                try {
                    workoutTrackerServerService.addExercises(uId, localDatabaseExercises)
                } catch(e: Exception) {
                    // make local exercises pending upload
                    throw e
                }
            } catch(e: Exception) {
                Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
            }
            val editor = eventFlagsSharedPreferences.edit()
            editor.putStringSet("userHasLoggedIn", ((usersThatHaveLoggedIn ?: emptySet()) + (uId)))
            editor.apply()
        }
    }


    // TEMP
    suspend fun createFirebaseWorkout(uId: String, workout: Workout) {
        workoutTrackerServerService.addWorkout(uId, workout)
    }

    suspend fun createFirebaseExercise(uId: String, exercise: Exercise) {
        workoutTrackerServerService.addExercise(uId, exercise)
    }

    fun getAllFirebaseWorkouts(uId: String): Flow<Response<List<Workout>>> = flow {
        emit(Response.Loading())
        try {
            val workouts = workoutTrackerServerService.getWorkouts(uId)
            emit(Response.Success(workouts))
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage))
        }
    }

    fun getAllFirebaseExercises(uId: String): Flow<Response<List<Exercise>>> = flow {
        emit(Response.Loading())
        try {
            val exercises = workoutTrackerServerService.getExercises(uId)
            emit(Response.Success(exercises))
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage))
        }
    }
    // TEMP

    // SharedPreferences
    fun addPendingUpload(uId: String?, key: String, value: String) {
        val editor = pendingUpload.edit()
        editor.putString("$uId:$key", value)
        editor.apply()
    }

    fun addPendingDeletion(uId: String?, key: String, value: String) {
        val editor = pendingDeletion.edit()
        editor.putString("$uId:$key", value)
        editor.apply()
    }

    fun getFirebaseUser(): FirebaseUser? {
        return authClient.getCurrentUser()
    }

    suspend fun createAccount(email: String, password: String): Flow<Response<FirebaseUser?>> = flow {
        emit(Response.Loading())
        try {
            val user = authClient.createAccount(email, password)
            emit(Response.Success(user))
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage))
        }
    }

    suspend fun signIn(email: String, password: String): Flow<Response<FirebaseUser?>> = flow {
        emit(Response.Loading())

        try {
            val user = authClient.signIn(email, password)
            emit(Response.Success(user))
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage))
        }
    }

    suspend fun signOut() {
        return withContext(dispatcher) {
            authClient.signOut()
        }
    }

    fun getAllExercises(): Flow<Response<List<Exercise>>> = flow {
        emit(Response.Loading())

        try {
            var exercises = database.exerciseDao.getAllExercises()
            Log.d("getAll", "size: ${exercises.count()}")

            if (exercises.isEmpty()) {
                Log.d("getAll", "isEmpty")
                val fetchedExercises = networkService.getExercises(10000)
                database.exerciseDao.upsertExercises(fetchedExercises)
                exercises = database.exerciseDao.getAllExercises()

                saveExerciseGifsToFile(application, exercises)
            }

            emit(Response.Success(exercises))

        } catch (e: Exception) {
            Log.d("getAll", "Msg: ${e.message}")
            emit(Response.Error(e.message))
        }
    }

    suspend fun getExercise(withId: String): Exercise {
        return withContext(Dispatchers.IO) {
            database.exerciseDao.getExercise(withId)
        }
    }

    suspend fun deleteAllHistory() {
        withContext(Dispatchers.IO) {
            database.workoutDao.deleteAllWorkouts()
        }
    }

    suspend fun updateExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.updateExercise(exercise)
        }
    }

    suspend fun updateExercises(exercises: List<WorkoutSet>) {
        withContext(Dispatchers.IO) {
            exercises.forEach {
                database.exerciseDao.updateExerciseById(
                    timeStampLastClicked = it.timestamp ?: System.currentTimeMillis(),
                    prevLbs = it.previousLbs,
                    prevReps = it.previousReps,
                    newPrevLbs = it.previousLbs,
                    newPrevReps = it.previousReps,
                    id = it.exerciseId
                )
            }
        }
    }

    suspend fun saveNewExercise(uId: String?, exercise: Exercise) {
        withContext(Dispatchers.IO) {
            database.exerciseDao.upsert(exercise)
            uId?.let {
                if (isNetworkConnected(application)) {
                    try {
                        workoutTrackerServerService.addExercise(uId, exercise)
                    } catch (e: Exception) {
                        addPendingUpload(uId, exercise.id, "exercise")
                    }
                } else {
                    addPendingUpload(uId, exercise.id, "exercise")
                }
            }
        }
    }

    suspend fun saveNewWorkout(workout: Workout) {
        withContext(Dispatchers.IO) {
            database.workoutDao.upsert(workout)
        }
    }

    suspend fun getAllWorkouts(): List<Workout> {
        return withContext(Dispatchers.IO) {
            val all = database.workoutDao.getAllWorkouts()
            all.ifEmpty {
                listOf(testWorkout, testWorkout, testWorkout.copy(startTime = System.currentTimeMillis() - 30000000), testWorkout.copy(startTime = 1000003))
            }

        }
    }

    suspend fun saveExerciseGifsToFile(context: Context, exercises: List<Exercise>) {
        withContext(Dispatchers.IO) {
            try {
                val directory = File(context.filesDir, "gifs")

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                exercises.forEach {
                    val file = File(directory, it.id)

                    val url = URL(it.gifUrl)
                    val connection = url.openConnection()
                    connection.connect()

                    val input: InputStream = connection.getInputStream()
                    val output = FileOutputStream(file)

                    val buffer = ByteArray(1024)
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                    }

                    output.close()
                    input.close()
                }
            } catch (e: Exception) {
                Log.d("gif", "Error: $e")
                e.printStackTrace()
            }
        }
    }

    suspend fun saveGifToFile(context: Context, gifUrl: String, fileName: String) {
        withContext(Dispatchers.IO) {
            try {
                // Create a directory in the app's internal storage
                val directory = File(context.filesDir, "gifs")

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                // Create a file for the downloaded GIF
                val file = File(directory, fileName)

                // Download the GIF from the URL
                val url = URL(gifUrl)
                val connection = url.openConnection()
                connection.connect()

                // Save the GIF to the file using FileOutputStream
                val input: InputStream = connection.getInputStream()
                val output = FileOutputStream(file)

                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }

                output.close()
                input.close()

            } catch (e: Exception) {
                Log.d("gif", "Error: $e")
                e.printStackTrace()
            }
        }
    }

    suspend fun getGifs(context: Context): Map<String, File?> {
        try {
            return File(context.filesDir, "gifs").listFiles().associateBy { it.name }
        } catch (e: Exception) {
            Log.d("getGifs", "Error: ${e.message}")
            return emptyMap()
        }
    }

    suspend fun getGif(context: Context, fileName: String): File? {
        val directory = File(context.filesDir, "gifs")

        val file = File(directory, fileName)
        return if (file.exists()) {
            file
        } else {
            Log.d("gif", "return null")
            null
        }
    }
}