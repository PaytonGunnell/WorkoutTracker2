package com.paytongunnell.workouttracker2.screens.authtest

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.network.WorkoutTrackerServerService
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository

class LoadingTestViewModel(
    application: Application
): AndroidViewModel(application) {

    val repository = WorkoutTrackerRepository(ExerciseDBService, FirebaseAuthClient, ExerciseDatabase.getInstance(application))

    private var _user by mutableStateOf<FirebaseUser?>(null)
    val user: FirebaseUser?
        get() = _user

    private val sharedPreferences = application.getSharedPreferences("Event_Flags", Context.MODE_PRIVATE)

    init {
        _user = repository.getFirebaseUser()
    }

    fun hasSeenSignUpScreen(): Boolean {
        return sharedPreferences.getBoolean("showSignUpOnLaunch", true)
    }

    fun setHasSeenSignUpScreen() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("showSignUpOnLaunch", false)
        editor.apply()
    }
}