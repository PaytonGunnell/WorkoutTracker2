package com.paytongunnell.workouttracker2.screens.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import com.paytongunnell.workouttracker2.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
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
    }
}