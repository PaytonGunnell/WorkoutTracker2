package com.paytongunnell.workouttracker2.screens.loading

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingTestViewModel @Inject constructor(
    application: Application,
    private val repository: WorkoutTrackerRepository
): AndroidViewModel(application) {

    private var _user by mutableStateOf<FirebaseUser?>(null)
    val user: FirebaseUser?
        get() = _user

    private var _finished by mutableStateOf(false)
    val finished: Boolean
        get() = _finished

    init {
        _user = repository.getFirebaseUser()
        viewModelScope.launch {
            repository.downloadExercisesFromApi()
            _finished = true
        }
    }


    // Shared Preferences methods
    fun hasSeenSignUpScreen(): Boolean {
        return repository.hasSeenSignUpScreen()
    }

    fun setHasSeenSignUpScreen() {
        repository.setHasSeenSignUpScreen()
    }
}