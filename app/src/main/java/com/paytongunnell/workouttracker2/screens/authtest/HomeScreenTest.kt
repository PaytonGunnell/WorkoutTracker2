package com.paytongunnell.workouttracker2.screens.authtest

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseUser
import com.ramcosta.composedestinations.annotation.Destination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.screens.authtest.destinations.AuthTestScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HomeScreenTest(
    navigator: DestinationsNavigator,
    userId: String,
    viewModel: HomeScreenTestViewModel = hiltViewModel()
) {

    Column {
        Text(userId)
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
    }
}