package com.paytongunnell.workouttracker2.screens.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.paytongunnell.workouttracker2.screens.destinations.AuthScreenDestination
import com.paytongunnell.workouttracker2.screens.destinations.TabBarScreenDestination
import com.paytongunnell.workouttracker2.ui.theme.lighterGray
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun LoadingScreen(
    navigator: DestinationsNavigator,
    viewModel: LoadingTestViewModel = hiltViewModel()
) {

    var launch by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.finished) {
        if (launch) {
            if (viewModel.user == null && viewModel.hasSeenSignUpScreen()) {
                viewModel.setHasSeenSignUpScreen()
                navigator.navigate(AuthScreenDestination)
            } else {
                navigator.navigate(TabBarScreenDestination)
            }
        }
        launch = true
    }

    Column {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.lighterGray)
    }
}