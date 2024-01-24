package com.paytongunnell.workouttracker2.screens.authtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import kotlinx.coroutines.launch

class HomeScreenTestViewModel(
    application: Application
): AndroidViewModel(application) {

    private val repository = WorkoutTrackerRepository(ExerciseDBService, FirebaseAuthClient, ExerciseDatabase.getInstance(application))
    val user = repository.getFirebaseUser()

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}