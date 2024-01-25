package com.paytongunnell.workouttracker2.screens.authtest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.screens.authtest.destinations.AuthTestScreenDestination
import com.paytongunnell.workouttracker2.utils.Response
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreenTest(
    navigator: DestinationsNavigator,
    userId: String?,
    viewModel: HomeScreenTestViewModel = hiltViewModel()
) {

    val exercises by viewModel.exercises.collectAsStateWithLifecycle()
    val localExercises by viewModel.localExercises.collectAsStateWithLifecycle()

    val workouts by viewModel.workouts.collectAsStateWithLifecycle()

    Column {
        Text(userId ?: "null uid")

        Button(onClick = {
            viewModel.signOut()
            navigator.navigate(AuthTestScreenDestination)
        }) {
            if (viewModel.user == null) {
                Text("Sign Up or Log In")
            } else {
                Text("Log Out")
            }
        }

//        Button(onClick = { viewModel.createNewFirebsaeWorkout() }) {
//            Text("Create new Workout")
//        }
//
//        Button(onClick = { viewModel.getWorkoutsFromFirebase() }) {
//            Text("Get Firebase Workouts")
//        }

        Row {
            Box(modifier = Modifier.weight(1f)) {
                when (val state = localExercises) {
                    is Response.Loading -> {
                        CircularProgressIndicator()
                    }

                    is Response.Success -> {
                        LazyColumn {
                            items(state.data) { exercise ->
                                Text("${exercise.id}")
                                Divider()
                            }
                        }
                    }

                    is Response.Error -> {
                        Text(state.message ?: "Error")
                    }

                    else -> {
                        Text("null")
                    }
                }
            }

            Spacer(Modifier.weight(0.2f))

            Box(modifier = Modifier.weight(1f)) {
                when (val state = exercises) {
                    is Response.Loading -> {
                        CircularProgressIndicator()
                    }

                    is Response.Success -> {
                        LazyColumn {
                            items(state.data) { exercise ->
                                Text("${exercise.id}")
                                Divider()
                            }
                        }
                    }

                    is Response.Error -> {
                        Text(state.message ?: "Error")
                    }

                    else -> {
                        Text("null")
                    }
                }
            }
        }
    }
}