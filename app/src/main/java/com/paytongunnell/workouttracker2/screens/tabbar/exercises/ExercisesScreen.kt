package com.paytongunnell.workouttracker2.screens.tabbar.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.BodyPart
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.ExerciseEquipment
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.ExerciseList
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.FilterDropDownButton
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.SearchBar
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExercisesScreen(
    exercises: List<Exercise>,
    gifs: Map<String, File?>,
    onClickExerciseInfo: (Exercise) -> Unit,
    onClickNewExercise: () -> Unit,
    onCreateNewExercise: (Exercise) -> Unit,
    exercisesScreenViewModel: ExercisesScreenViewModel = viewModel(
        factory = ExercisesScreenViewModel.Factory(
            exercises = exercises
        )
    ),
    modifier: Modifier = Modifier
) {
    var showBodyPartFilter by remember { mutableStateOf(false) }
    var showEquipmentFilter by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchText by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .padding(top = 5.dp)
                    .clickable { }
            ) {

                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string._new), style = MaterialTheme.typography.blueTextButton, modifier = Modifier.clickable { onClickNewExercise() })
//                    Text(
//                        "Exercises",
//                        style = MaterialTheme.typography.titleSmall,
//                        modifier = Modifier
//                            .align(Alignment.CenterVertically)
//                    )
                    Text(stringResource(R.string._new), modifier = Modifier.alpha(0f))
                }


                Text(
                    stringResource(R.string.exercises),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Column(modifier = Modifier.padding(end = 5.dp)) {
                    // SearchBar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                    ) {
                        Surface(
                            modifier = Modifier
//                                .shadow(5.dp)
                                .fillMaxWidth()
                            ,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SearchBar(
                                    value = searchText,
                                    onValueChange = {
                                        searchText = it
                                        exercisesScreenViewModel.searchEquipment(it)
                                    },
                                    onSearch = {
                                               keyboardController?.hide()
                                    },
                                    modifier = Modifier
                                        .shadow(5.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .padding(5.dp)
                                        .weight(1f)
                                    ,
                                    hint = stringResource(R.string.search)
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)) {
                        FilterDropDownButton(
                            enumValues = enumValues<BodyPart>(),
                            onClickDropDownMenu = { showBodyPartFilter = true },
                            onClickFilterOption = { exercisesScreenViewModel.filterBodyPart(it) },
                            onDismiss = { showBodyPartFilter = false },
                            doExpand = showBodyPartFilter,
                            exerciseFilter = exercisesScreenViewModel.exerciseFilter,
                            type = BodyPart::class.java,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        FilterDropDownButton(
                            enumValues = enumValues<ExerciseEquipment>(),
                            onClickDropDownMenu = { showEquipmentFilter = true },
                            onClickFilterOption = { exercisesScreenViewModel.filterEquipment(it) },
                            onDismiss = { showEquipmentFilter = false },
                            doExpand = showEquipmentFilter,
                            exerciseFilter = exercisesScreenViewModel.exerciseFilter,
                            type = ExerciseEquipment::class.java,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                ExerciseList(
                    exercises = exercisesScreenViewModel.filteredExercises,
                    gifs = gifs,
                    onClickExercise = { onClickExerciseInfo(it) },
                    modifier = Modifier.weight(30f)
                )
            }
        }
    }
}