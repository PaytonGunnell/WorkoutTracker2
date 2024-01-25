package com.paytongunnell.workouttracker2.screens.authtest

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingTestViewModel @Inject constructor(
    application: Application,
    repository: WorkoutTrackerRepository
): AndroidViewModel(application) {

    private var _user by mutableStateOf<FirebaseUser?>(null)
    val user: FirebaseUser?
        get() = _user

    // Here im using SharedPreferences so that the user is only forced to see the sign up screen once
    private val eventFlagsSharedPreferences = application.getSharedPreferences("Event_Flags", Context.MODE_PRIVATE)

    init {
        _user = repository.getFirebaseUser()
    }


    // Shared Preferences methods
    fun hasSeenSignUpScreen(): Boolean {
        return eventFlagsSharedPreferences.getBoolean("showSignUpOnLaunch", true)
    }

    fun setHasSeenSignUpScreen() {
        val editor = eventFlagsSharedPreferences.edit()
        editor.putBoolean("showSignUpOnLaunch", false)
        editor.apply()
    }
}