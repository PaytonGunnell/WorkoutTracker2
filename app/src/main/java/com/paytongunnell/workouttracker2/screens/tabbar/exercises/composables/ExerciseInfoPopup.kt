package com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import com.paytongunnell.workouttracker2.ui.theme.lighterGray
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ExerciseInfoPopup(
    exercise: Exercise,
    gif: File?,
    onClose: () -> Unit,
    onClickOffExerciseInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var tabs = arrayOf("About", "History", "Charts", "Records")

    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            )
            .padding(horizontal = 15.dp)
            .padding(vertical = 10.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 2.dp)
                        .clickable { onClose() }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
                Text(
                    "${exercise.name}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Edit",
                    style = MaterialTheme.typography.blueTextButton.copy(Color.Black.copy(alpha = 0f))
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(bottom = 10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
                    .fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, s ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selectedIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 6.dp)
                            .clickable { selectedIndex = index }
                    ) {
                        Text(
                            s,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Box(modifier = Modifier) {
                when (selectedIndex) {
                    0 -> {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                GlideImage(
                                    model = gif,
                                    contentScale = ContentScale.FillBounds,
                                    contentDescription = null,
                                    loading = placeholder(R.drawable.dumbbell_icon),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.medium)
                                        .aspectRatio(1f)
                                )
                            }
                        item {
                            Column {
                                Text(
                                    "Instructions",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(vertical = 20.dp)
                                )
                                exercise.instructions?.forEachIndexed { index, s ->
                                    Text(
                                        "${index + 1}. $s",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.lighterGray),
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                }
                            }
                        }
                        }
                    }

                    1 -> {}
                    2 -> {}
                    3 -> {}
                }
            }
        }
    }
}
