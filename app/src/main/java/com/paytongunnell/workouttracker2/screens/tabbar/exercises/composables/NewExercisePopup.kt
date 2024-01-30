package com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paytongunnell.workouttracker2.model.BodyPart
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.ExerciseEquipment
import com.paytongunnell.workouttracker2.model.ExerciseFilter
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import java.util.UUID

@Composable
fun NewExercisePopup(
    onCreateNewExercise: (Exercise) -> Unit,
    onDismiss: () -> Unit,
    newExerciseViewModel: NewExerciseViewModel = viewModel(),
    modifier: Modifier = Modifier
) { // Name, BodyPart, Equipment

    var nameInput by remember { mutableStateOf("") }

    // DropDown toggles
    var showBodyPartSpecification by remember { mutableStateOf(false) }
    var showEquipmentSpecification by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 2.dp)
                        .clickable { onDismiss() }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
                Text(
                    "New",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Save",
                    style = MaterialTheme.typography.blueTextButton,
                    modifier = Modifier.clickable {
                        if (newExerciseViewModel.onClickSave()) {
                            onCreateNewExercise(newExerciseViewModel.newExercise)
                            onDismiss()
                        }
                    }
                )
            }

            Box(modifier = Modifier.padding(vertical = 10.dp)) {
                BasicTextField(
                    value = nameInput, onValueChange = {
                        nameInput = it
                        newExerciseViewModel.editExerciseName(it)
                    },
                    textStyle = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                if (nameInput == "") {
                    Text(
                        "Exercise Name",
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ChooseSpecificationsDropDown(
                    enumValues = enumValues<BodyPart>(),
                    onClickDropDownMenu = { showBodyPartSpecification = true },
                    onClickSpecification = { newExerciseViewModel.filterBodyPart(it) },
                    onDismiss = { showBodyPartSpecification = false },
                    doExpand = showBodyPartSpecification,
                    exerciseFilter = newExerciseViewModel.exerciseFilter,
                    type = BodyPart::class.java,
                    modifier = Modifier.weight(1f)
                )
                ChooseSpecificationsDropDown(
                    enumValues = enumValues<ExerciseEquipment>(),
                    onClickDropDownMenu = { showEquipmentSpecification = true },
                    onClickSpecification = { newExerciseViewModel.filterEquipment(it) },
                    onDismiss = { showEquipmentSpecification = false },
                    doExpand = showEquipmentSpecification,
                    exerciseFilter = newExerciseViewModel.exerciseFilter,
                    type = ExerciseEquipment::class.java,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

class NewExerciseViewModel: ViewModel() {

    private var _newExercise by mutableStateOf(Exercise(id = UUID.randomUUID().toString(), name = ""))
    val newExercise: Exercise
        get() = _newExercise

    private var _exerciseFilter by mutableStateOf(ExerciseFilter())
    var exerciseFilter: ExerciseFilter
        get() = _exerciseFilter
        set(value) {
            _exerciseFilter = value
        }

    fun filterBodyPart(bodyPart: BodyPart) {
        if (bodyPart == exerciseFilter.bodyPart) {
            exerciseFilter = exerciseFilter.copy(bodyPart = null)
        } else {
            exerciseFilter = exerciseFilter.copy(bodyPart = bodyPart)
        }
    }

    fun filterEquipment(equipment: ExerciseEquipment) {
        if (equipment == exerciseFilter.equipment) {
            exerciseFilter = exerciseFilter.copy(equipment = null)
        } else {
            exerciseFilter = exerciseFilter.copy(equipment = equipment)
        }
    }

    fun editExerciseName(name: String) {
        _newExercise = _newExercise.copy(name = name)
    }
    fun editExerciseBodyPart(bodyPart: BodyPart? = null) {
        _newExercise = _newExercise.copy(bodyPart = bodyPart)
    }
    fun editExerciseEquipment(equipment: ExerciseEquipment? = null) {
        _newExercise = _newExercise.copy(equipment = equipment)
    }
    fun onClickSave(): Boolean { // can save
        return _newExercise.name != ""
    }
}