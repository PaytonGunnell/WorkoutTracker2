package com.paytongunnell.workouttracker2.screens.authtest

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.screens.authtest.destinations.HomeScreenTestDestination
import com.paytongunnell.workouttracker2.utils.Response
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AuthTestScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthTestViewModel = hiltViewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()

    when (val userState = user) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            run {
                navigator.navigate(HomeScreenTestDestination(userState.data?.uid))
            }
        }
        else -> {
            Column {
                var email by remember { mutableStateOf("") }
                TextField(value = email, onValueChange = {
                    email = it
                })

                var password by remember { mutableStateOf("") }
                TextField(value = password, onValueChange = {
                    password = it
                })

                Button(onClick = { viewModel.createAccount(email, password) }) {
                    Text("Sign Up")
                }

                Button(onClick = { viewModel.signIn(email, password) }) {
                    Text("Log In")
                }

                Button(onClick = { /*TODO*/ }) {
                    Text("Continue without an account")
                }

                if (userState is Response.Error) {
                    Text("Error: ${userState.message}")
                }
            }
        }
    }
}