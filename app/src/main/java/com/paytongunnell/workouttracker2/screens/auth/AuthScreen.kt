package com.paytongunnell.workouttracker2.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.screens.auth.composables.AuthButton
import com.paytongunnell.workouttracker2.screens.destinations.TabBarScreenDestination
import com.paytongunnell.workouttracker2.ui.theme.lighterGray
import com.paytongunnell.workouttracker2.utils.Response
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()

    when (val userState = user) {
        is Response.Loading -> {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.lighterGray)
        }
        is Response.Success -> {
            run {
                navigator.navigate(TabBarScreenDestination)
            }
        }
        else -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = { Text(stringResource(R.string.email)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    )

                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = { Text(stringResource(R.string.password)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    AuthButton(
                        onClick = { viewModel.createAccount(email, password) },
                        text = stringResource(R.string.sign_up),
                    )

                    AuthButton(
                        onClick = { viewModel.signIn(email, password) },
                        text = stringResource(R.string.sign_in),
                    )

                    Button(
                        onClick = { viewModel.continueWithoutAccount() },
                    ) {
                        Text("Continue without an account")
                    }

                    if (userState is Response.Error) {
                        Text("Error: ${userState.message}", modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}