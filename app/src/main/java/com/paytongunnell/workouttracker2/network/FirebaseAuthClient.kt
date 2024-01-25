package com.paytongunnell.workouttracker2.network

import android.content.Context
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.IllegalArgumentException
import kotlin.coroutines.resume

object FirebaseAuthClient {

    private var auth: FirebaseAuth = Firebase.auth

//    private var _user by mutableStateOf<FirebaseUser?>(null)
//    val user: FirebaseUser?
//        get() = _user

//    init {
//        _user = auth.currentUser
//    }


    fun getCurrentUser(): FirebaseUser? {
        Log.d("fbauth", "getCurrentUser:called")
        return auth.currentUser
    }

    suspend fun createAccount(email: String, password: String): FirebaseUser = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("fbauth", "createUserWithEamil:success")
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Log.d("fbauth", "signIn:success")
                                auth.currentUser?.let { user ->
                                    continuation.resume(user)
                                }
                            } else {
                                Log.d("fbauth", "signIn:failure")
                                continuation.cancel(task2.exception)
                            }
                        }
                } else {
                    Log.d("fbauth", "createUserWithEamil:failure")
                    continuation.cancel(task.exception)
                }
            }
    }

    suspend fun signIn(email: String, password: String): FirebaseUser = suspendCancellableCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("fbauth", "signIn:success")
                    auth.currentUser?.let { user ->
                        continuation.resume(user)
                    }
                } else {
                    Log.d("fbauth", "signIn:failure")
                    continuation.cancel(task.exception)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
}