package com.paytongunnell.workouttracker2.screens.authtest

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.paytongunnell.workouttracker2.backgroundwork.UploadWorker
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenTestViewModel @Inject constructor(
    application: Application,
    private val repository: WorkoutTrackerRepository
): AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)
//    private val repository = WorkoutTrackerRepository(ExerciseDBService, FirebaseAuthClient, ExerciseDatabase.getInstance(application))
    val user = repository.getFirebaseUser()

    var a = 0

    init {
        timer()
    }

    fun timer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                Log.d("connreceiver", "viewModel still initialized")
            }
        }
    }

    val uploadWorkRequest: WorkRequest =
        OneTimeWorkRequestBuilder<UploadWorker>()
            .build()

    fun sumbitWorkRequest() {
        workManager
            .enqueue(uploadWorkRequest)
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}