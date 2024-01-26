package com.paytongunnell.workouttracker2.repository

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.network.WorkoutTrackerServerService
import com.paytongunnell.workouttracker2.utils.Response
import com.paytongunnell.workouttracker2.utils.UploadTo
import com.paytongunnell.workouttracker2.utils.UploadType
import com.paytongunnell.workouttracker2.utils.isNetworkConnected
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

class WorkoutTrackerRepository constructor(
    private val application: Application,
    private val networkService: ExerciseDBService,
    private val authClient: FirebaseAuthClient,
    private val workoutTrackerServerService: WorkoutTrackerServerService,
    private val database: ExerciseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    
    // Stores the id's of all exercises or workouts deleted/created while offline so that when the app is back online the
    // firebase data will be synced with the local data
    private val pendingDeletion = application.getSharedPreferences("Pending_Deletion", Context.MODE_PRIVATE)
    private val pendingUpload = application.getSharedPreferences("Pending_Upload", Context.MODE_PRIVATE)

    // Here im using SharedPreferences so that the user is only forced to see the sign up screen once
    private val eventFlagsSharedPreferences = application.getSharedPreferences("Event_Flags", Context.MODE_PRIVATE)

    suspend fun deleteExercise(exerciseId: String) {
        withContext(dispatcher) {
            val user = authClient.getCurrentUser()

            database.exerciseDao.deleteExerciseWithId(exerciseId)

            user?.uid?.let { uId ->
                if (isNetworkConnected(application)) {
                    try {
                        workoutTrackerServerService.deleteExercise(uId, exerciseId)
                    } catch (e: Exception) {
                        // add exercise to pending deletion
                        addPendingDeletion(uId, UploadType.EXERCISE, exerciseId)
                    }
                } else {
                    // add exercise to pending deletion
                    addPendingDeletion(uId, UploadType.EXERCISE, exerciseId)
                }
            }
        }
    }

    suspend fun deleteWorkout(workoutId: String) {
        withContext(dispatcher) {
            val user = authClient.getCurrentUser()

            database.workoutDao.deleteWorkoutWithId(workoutId)

            user?.uid?.let { uId ->
                if (isNetworkConnected(application)) {
                    try {
                        workoutTrackerServerService.deleteWorkout(uId, workoutId)
                    } catch (e: Exception) {
                        // add exercise to pending deletion
                        addPendingDeletion(uId, UploadType.WORKOUT, workoutId)
                    }
                } else {
                    // add exercise to pending deletion
                    addPendingDeletion(uId, UploadType.WORKOUT, workoutId)
                }
            }
        }
    }

    // BroadcastReceiverSync
    suspend fun syncFirebaseAfterBackOnline() {
        withContext(dispatcher) {
            try {
                val user = authClient.getCurrentUser()

                user?.uid?.let { uId ->
                    // Upload Exercises
                    val exerciseIdsToBeUploaded = pendingUpload.getStringSet("${uId}:${UploadType.EXERCISE.stringValue}:${UploadTo.FIREBASE.stringValue}", null)?.toList()

                    exerciseIdsToBeUploaded?.let { ids ->
                        Log.d("repository", "exerciseids not null")
                        val exercises = database.exerciseDao.getExercisesWithIds(ids)
                        workoutTrackerServerService.addExercises(uId, exercises)

                        // set list of exercise id's to null
                        addListPendingUpload(uId, UploadType.EXERCISE, UploadTo.FIREBASE, null)
                    }

                    // Upload Workouts
                    val workoutIdsToBeUploaded = pendingUpload.getStringSet("$uId:${UploadType.EXERCISE.stringValue}:${UploadTo.FIREBASE.stringValue}", null)?.toList()

                    workoutIdsToBeUploaded?.let { ids ->
                        val workouts = database.workoutDao.getWorkoutsWithIds(ids)
                        workoutTrackerServerService.addWorkouts(uId, workouts)

                        // set list of workout id's to null
                        addListPendingUpload(uId, UploadType.WORKOUT, UploadTo.FIREBASE, null)
                    }

                    // Delete Exercises
                    val exerciseIdsToBeDeleted = pendingDeletion.getStringSet("$uId:${UploadType.EXERCISE.stringValue}", null)?.toList()

                    exerciseIdsToBeDeleted?.let { ids ->
                        workoutTrackerServerService.deleteExercises(uId, ids)

                        // set list of exercise id's to null
                        addListPendingDeletion(uId, UploadType.EXERCISE, null)
                    }

                    // Delete Workouts
                    val workoutIdsToBeDeleted = pendingDeletion.getStringSet("$uId:${UploadType.WORKOUT.stringValue}", null)?.toList()

                    workoutIdsToBeDeleted?.let { ids ->
                        workoutTrackerServerService.deleteWorkouts(uId, ids)

                        // set list of workout id's to null
                        addListPendingDeletion(uId, UploadType.WORKOUT, null)
                    }
                }
            } catch(e: Exception) {
                Log.d("repository", "Error: ${e.message}")
            }
        }
    }

    // Firebase

    fun getFirebaseUser(): FirebaseUser? {
        return authClient.getCurrentUser()
    }
    suspend fun createAccount(email: String, password: String): Flow<Response<FirebaseUser?>> = flow {
        withContext(dispatcher) {
            emit(Response.Loading())
            try {
                val user = authClient.createAccount(email, password)
                emit(Response.Success(user))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage))
            }
        }
    }
    suspend fun signIn(email: String, password: String): Flow<Response<FirebaseUser?>> = flow {
        withContext(dispatcher) {
            emit(Response.Loading())

            try {
                val user = authClient.signIn(email, password)
                emit(Response.Success(user))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage))
            }
        }
    }
    suspend fun signOut() {
        return withContext(dispatcher) {
            authClient.signOut()
        }
    }

    // will get the firebase data associated with the user and save it to the local database if it is their first time signing in on the device
    suspend fun syncLocalAndFirebaseDataIfFirstTimeSigningIn(transferData: Boolean = false) {
        withContext(dispatcher) {
            val user =  authClient.getCurrentUser()

            user?.uid?.let { uId ->
                val usersThatHaveLoggedIn = eventFlagsSharedPreferences.getStringSet("userHasLoggedIn", emptySet())
                if (usersThatHaveLoggedIn?.contains(uId) != true) {
                    try {
                        val firebaseExercises = workoutTrackerServerService.getExercises(uId)

                        if (transferData) {
                            val localDatabaseExercises = database.exerciseDao.getAllCustomMadeExercises()

                            if (localDatabaseExercises.isNotEmpty()) {
                                try {
                                    workoutTrackerServerService.addExercises(uId, localDatabaseExercises)
                                } catch (e: Exception) {
                                    // make local exercises pending upload
                                    addListPendingUpload(uId, UploadType.EXERCISE, UploadTo.FIREBASE, localDatabaseExercises.map { it.id }.toSet())

                                    throw e
                                }
                            }
                        }

                        firebaseExercises?.let {
                            try {
                                    database.exerciseDao.upsertExercises(firebaseExercises)
                            } catch(e: Exception) {
                                // make firebase exercises pending upload
                                addListPendingUpload(uId, UploadType.EXERCISE, UploadTo.LOCAL,firebaseExercises.map { it.id }.toSet())

                                throw e
                            }
                        }

                        val firebaseWorkouts = workoutTrackerServerService.getWorkouts(uId)

                        if (transferData) {
                            val localDatabaseWorkouts = database.workoutDao.getAllWorkouts()
                            if (localDatabaseWorkouts.isNotEmpty()) {
                                try {
                                    workoutTrackerServerService.addWorkouts(uId, localDatabaseWorkouts)
                                } catch (e: Exception) {
                                    // make local workouts pending upload
                                    addListPendingUpload(uId, UploadType.WORKOUT, UploadTo.FIREBASE, localDatabaseWorkouts.map { it.id }.toSet())

                                    throw e
                                }
                            }
                        }

                        firebaseWorkouts?.let {
                            try {
                                    database.workoutDao.upsertWorkouts(firebaseWorkouts)

                            } catch(e: Exception) {
                                // make firebase workouts pending upload
                                addListPendingUpload(uId, UploadType.WORKOUT, UploadTo.LOCAL, firebaseWorkouts.map { it.id }.toSet())

                                throw e
                            }
                        }


                    } catch(e: Exception) {
                        Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
                    }

                    val editor = eventFlagsSharedPreferences.edit()
                    editor.putStringSet("userHasLoggedIn", ((usersThatHaveLoggedIn ?: emptySet()) + (uId)))
                    editor.apply()
                } else {
                    Log.d("repository", "user has logged in on device")
                }
            }
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
            if (workouts != null) {
                emit(Response.Success(workouts))
            } else {
                emit(Response.Error("No Workouts"))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage))
        }
    }
    fun getAllFirebaseExercises(uId: String): Flow<Response<List<Exercise>>> = flow {
        emit(Response.Loading())
        try {
            if (isNetworkConnected(application)) {
                val exercises = workoutTrackerServerService.getExercises(uId)
                if (exercises != null) {
                    emit(Response.Success(exercises))
                } else {
                    emit(Response.Error("No Exercises"))
                }
            } else {
                throw IllegalArgumentException("Connect To The Internet")
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage))
        }
    }
    // TEMP

    // SharedPreferences
    fun hasSeenSignUpScreen(): Boolean {
        return eventFlagsSharedPreferences.getBoolean("showSignUpOnLaunch", true)
    }
    fun setHasSeenSignUpScreen() {
        val editor = eventFlagsSharedPreferences.edit()
        editor.putBoolean("showSignUpOnLaunch", false)
        editor.apply()
    }
    fun addPendingUpload(uId: String, uploadType: UploadType, uploadTo: UploadTo, id: String) {
        val idsPendingUpload = pendingUpload.getStringSet("$uId:${uploadType.stringValue}:${uploadTo.stringValue}", emptySet())
        val editor = pendingUpload.edit()
        editor.putStringSet("$uId:${uploadType.stringValue}:${uploadTo.stringValue}", idsPendingUpload?.plus(id))
        editor.apply()
    }
    fun addListPendingUpload(uId: String, uploadType: UploadType, uploadTo: UploadTo, idList: Set<String>?) {
        val editor = pendingUpload.edit()
        editor.putStringSet("$uId:${uploadType.stringValue}:${uploadTo.stringValue}", idList)
        editor.apply()
    }
    fun addPendingDeletion(uId: String, uploadType: UploadType, id: String) {
        val idsPendingDeletion = pendingDeletion.getStringSet("$uId:${uploadType.stringValue}", emptySet())
        val editor = pendingDeletion.edit()
        editor.putStringSet("$uId:${uploadType.stringValue}", idsPendingDeletion?.plus(id))
        editor.apply()
    }
    fun addListPendingDeletion(uId: String, uploadType: UploadType, idList: Set<String>?) {
        val editor = pendingDeletion.edit()
        editor.putStringSet("$uId:${uploadType.stringValue}", idList)
        editor.apply()
    }

    // API
    suspend fun downloadExercisesFromApi() {
        withContext(dispatcher) {
            try {
                val count = database.exerciseDao.getCount()

                if (count <= 0) {
                    val fetchedExercises = networkService.getExercises()
                    database.exerciseDao.upsertExercises(fetchedExercises)

                    val exercises = database.exerciseDao.getAllExercises()
                    saveExerciseGifsToFile(application, exercises)
                }
            } catch (e: Exception) {
                Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Local Database
    fun getAllCustomMadeExercises(): Flow<Response<List<Exercise>>> = flow {
        withContext(dispatcher) {
            emit(Response.Loading())

            try {
                var exercises = database.exerciseDao.getAllCustomMadeExercises()
                emit(Response.Success(exercises))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage))
            }
        }
    }
    fun getAllLocalExercises(): Flow<Response<List<Exercise>>> = flow {
        withContext(dispatcher) {
            emit(Response.Loading())

            try {
                var exercises = database.exerciseDao.getAllExercises()
                Log.d("getAll", "size: ${exercises.count()}")

                if (exercises.isEmpty()) {
                    Log.d("getAll", "isEmpty")
                    val fetchedExercises = networkService.getExercises()
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
    }
    suspend fun getAllLocalWorkouts(): List<Workout> {
        return withContext(dispatcher) {
            database.workoutDao.getAllWorkouts()
        }
    }
    suspend fun getExercise(withId: String): Exercise {
        return withContext(dispatcher) {
            database.exerciseDao.getExercise(withId)
        }
    }
    suspend fun deleteAllWorkouts() {
        withContext(dispatcher) {
            database.workoutDao.deleteAllWorkouts()
        }
    }
    suspend fun updateExercise(exercise: Exercise) {
        withContext(dispatcher) {
            database.exerciseDao.updateExercise(exercise)
        }
    }

//    suspend fun updateExercises(exercises: List<WorkoutSet>) {
//        withContext(Dispatchers.IO) {
//            exercises.forEach {
//                database.exerciseDao.upsertExercises(exercises)
//                database.exerciseDao.updateExerciseById(
//                    timeStampLastClicked = it.timestamp ?: System.currentTimeMillis(),
//                    prevLbs = it.previousLbs,
//                    prevReps = it.previousReps,
//                    newPrevLbs = it.previousLbs,
//                    newPrevReps = it.previousReps,
//                    id = it.exerciseId
//                )
//            }
//        }
//    }

    suspend fun createNewExercise(exercise: Exercise) {
        withContext(dispatcher) {
            database.exerciseDao.upsert(exercise)

            val user = authClient.getCurrentUser()

            user?.uid?.let { uId ->
                if (isNetworkConnected(application)) {
                    try {
                        workoutTrackerServerService.addExercise(uId, exercise)
                    } catch (e: Exception) {
                        addPendingUpload(uId, UploadType.EXERCISE, UploadTo.FIREBASE, exercise.id)
                        Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    addPendingUpload(uId, UploadType.EXERCISE, UploadTo.FIREBASE, exercise.id)
                }
            }
        }
    }

    suspend fun createNewWorkout(workout: Workout) {
        withContext(dispatcher) {
            database.workoutDao.upsert(workout)

            val user = authClient.getCurrentUser()

            user?.uid?.let { uId ->
                if (isNetworkConnected(application)) {
                    try {
                        workoutTrackerServerService.addWorkout(uId, workout)
                    } catch (e: Exception) {
                        addPendingUpload(uId, UploadType.WORKOUT, UploadTo.FIREBASE, workout.id)
                        Toast.makeText(application, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    addPendingUpload(uId, UploadType.WORKOUT, UploadTo.FIREBASE, workout.id)
                }
            }
        }
    }

    suspend fun saveExerciseGifsToFile(context: Context, exercises: List<Exercise>) {
        withContext(dispatcher) {
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
        withContext(dispatcher) {
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