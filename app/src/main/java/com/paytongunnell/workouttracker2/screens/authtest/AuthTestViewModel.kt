package com.paytongunnell.workouttracker2.screens.authtest

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import com.paytongunnell.workouttracker2.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthTestViewModel @Inject constructor(
    application: Application,
    private val repository: WorkoutTrackerRepository
): AndroidViewModel(application) {

    private var _user = MutableStateFlow<Response<FirebaseUser?>?>(null)
    val user: StateFlow<Response<FirebaseUser?>?> = _user.asStateFlow()

    fun continueWithoutAccount() {
        _user.value = Response.Success(null)
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            repository.createAccount(email, password)
                .collect {
                    _user.value = it
                }
        }
        Log.d("authtest", "user: ${_user}")
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            repository.signIn(email, password)
                .collect {
                    when (it) {
                        is Response.Loading -> {
                            _user.value = it
                        }
                        is Response.Success -> {
                            _user.value = it
                        }
                        is Response.Error -> {
                            _user.value = it
                        }
                    }
                }
        }
        Log.d("authtest", "user: ${_user}")
    }
}