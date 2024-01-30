package com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.BodyPart
import com.paytongunnell.workouttracker2.model.ExerciseEquipment
import com.paytongunnell.workouttracker2.model.ExerciseFilter
import com.paytongunnell.workouttracker2.ui.theme.lightBlueButton

@Composable
fun <T> FilterDropDownButton(
    enumValues: Array<T>,
    onClickDropDownMenu: () -> Unit,
    onClickFilterOption: (T) -> Unit,
    onDismiss: () -> Unit,
    doExpand: Boolean,
    exerciseFilter: ExerciseFilter,
    type: Class<T>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        DropdownMenu(
            expanded = doExpand,
            onDismissRequest = { onDismiss() },
        ) {
            enumValues.forEach {
                val selected = if (type.isAssignableFrom(BodyPart::class.java)) {
                    it == exerciseFilter.bodyPart
                } else { it == exerciseFilter.equipment }
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                (it as? BodyPart)?.stringValue?.uppercase()
                                    ?: (it as ExerciseEquipment).stringValue.uppercase(),
                                color = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary
                            )
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.alpha(if (selected) 1f else 0f)
                            )
                        }
                    },
                    onClick = {
                        onClickFilterOption(it)
                        onDismiss()
                              },
                    contentPadding = PaddingValues(0.dp)
                )
            }
        }
        if (type.isAssignableFrom(BodyPart::class.java)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .clickable { onClickDropDownMenu() }
                .background(
                    color = if (exerciseFilter.bodyPart != null) MaterialTheme.colorScheme.lightBlueButton else MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(3.dp)
            ) {
                Text(
                    exerciseFilter.bodyPart?.stringValue ?: stringResource(R.string.any_category),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .background(
                    color = if (exerciseFilter.equipment != null) MaterialTheme.colorScheme.lightBlueButton else MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(3.dp)
                .clickable { onClickDropDownMenu() }
            ) {
                Text(
                    exerciseFilter.equipment?.stringValue ?: stringResource(R.string.any_equipment),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}