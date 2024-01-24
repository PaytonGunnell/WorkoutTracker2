package com.paytongunnell.workouttracker2.screens.authtest

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.screens.authtest.destinations.AuthTestScreenDestination
import com.paytongunnell.workouttracker2.screens.authtest.destinations.HomeScreenTestDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope

@RootNavGraph(start = true)
@Destination
@Composable
fun LoadingTestScreen(
    navigator: DestinationsNavigator,
    viewModel: LoadingTestViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        if (viewModel.user == null && viewModel.hasSeenSignUpScreen()) {
            viewModel.setHasSeenSignUpScreen()
            navigator.navigate(AuthTestScreenDestination())
        } else {
            navigator.navigate(HomeScreenTestDestination(viewModel.user?.uid ?: "null uid"))
        }
    }

    CircularProgressIndicator()
}