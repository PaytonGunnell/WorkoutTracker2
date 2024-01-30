package com.paytongunnell.workouttracker2.screens.tabbar.startworkout

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.ui.theme.lightBlueButton
import java.io.File

@SuppressLint("UnrememberedMutableState")
@Composable
fun StartWorkoutScreen(
    exercises: List<Exercise>,
    gifs: Map<String, File?>,
    onStartWorkout: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp)
            .padding(horizontal = 10.dp)
    ) {
        item {
            Text(
                stringResource(R.string.start_workout),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .padding(bottom = 10.dp)
            )
        }
        item {
            Column {
                Text(stringResource(R.string.quick_start), style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 5.dp))
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.lightBlueButton,
                            shape = MaterialTheme.shapes.medium
                        )
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { onStartWorkout() }
                ) {
                    Text(
                        stringResource(R.string.start_an_empty_workout),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}
