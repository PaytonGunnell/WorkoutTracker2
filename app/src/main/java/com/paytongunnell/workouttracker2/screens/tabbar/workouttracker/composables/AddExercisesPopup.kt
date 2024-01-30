package com.paytongunnell.workouttracker2.screens.tabbar.workouttracker.composables

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.model.BodyPart
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.ExerciseEquipment
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.ExercisesScreenViewModel
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.ExerciseList
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.FilterDropDownButton
import com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables.SearchBar
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import com.paytongunnell.workouttracker2.ui.theme.lightBlueButton
import java.io.File

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AddExercisesPopup(
    exercises: List<Exercise>,
    gifs: Map<String, File?>,
    onAddExercises: (List<Exercise>) -> Unit,
    onCancel: () -> Unit,
    exercisesScreenViewModel: ExercisesScreenViewModel = viewModel(
        factory = ExercisesScreenViewModel.Factory(
            exercises = exercises
        )
    ),
    modifier: Modifier = Modifier
) {
    val grouping = exercises.groupBy { it.name.first().uppercase() }
    var selectedExercises = remember { mutableStateListOf<Exercise>() }

    var showBodyPartFilter by remember { mutableStateOf(false) }
    var showEquipmentFilter by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
//            .padding(horizontal = 20.dp)
            .padding(horizontal = 2.dp)
            .padding(vertical = 10.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp).padding(horizontal = 18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 2.dp)
                        .clickable { onCancel() }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
                Text(
                    "New",
                    style = MaterialTheme.typography.blueTextButton
                )
                Text(
                    text = if (selectedExercises.count() > 1)
                        "Add (${selectedExercises.count()})" else
                        "Add",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (selectedExercises.isNotEmpty())
                            MaterialTheme.colorScheme.lightBlueButton else
                            MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.clickable(enabled = selectedExercises.isNotEmpty()) {
                        onAddExercises(selectedExercises)
                        onCancel()
                    }
                )
            }

            Column(modifier = Modifier.fillMaxWidth().padding(end = 5.dp).padding(horizontal = 18.dp)) {
                // SearchBar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                ) {
                    Surface(
                        modifier = Modifier
                                .shadow(5.dp)
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
                                hint = "Search"
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
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
                onClickExercise = {
                    if (!selectedExercises.contains(it)) {
                        selectedExercises.add(it)
                    } else {
                        selectedExercises.remove(it)
                    }
                                  },
                selectedExercises = selectedExercises,
                modifier = Modifier.weight(30f)
            )
        }
    }
}