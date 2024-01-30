package com.paytongunnell.workouttracker2.screens.tabbar.exercises.composables

import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.ui.theme.lightBlueButton
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseList(
    exercises: List<Exercise>,
    gifs: Map<String, File?>,
    onClickExercise: (Exercise) -> Unit,
    selectedExercises: List<Exercise> = listOf(),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val grouping = exercises.groupBy { exercise ->
        if (exercise.name.first().isDigit()) {
            exercise.name.dropWhile { !it.isLetter() }.first().uppercase()
        } else {
            exercise.name.first().uppercase()
        }
    }.toList().sortedBy { it.first }

    var listState = rememberLazyListState()

    Row(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(30f)
        ) {
            grouping.forEach { (letter, items) ->
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = letter.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground)
                        )
                    }
                }

                items.forEach { item ->
                    item {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClickExercise(item) }
                            .background( color = MaterialTheme.colorScheme.lightBlueButton.copy(alpha = if (selectedExercises.contains(item)) 0.3f else 0f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                var img: ImageBitmap? = null
                                try {
                                    img = BitmapFactory.decodeFile(gifs[item.id]?.absolutePath ?: null)
                                        .asImageBitmap()
                                } catch (e: Exception) {

                                }

                                img?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(MaterialTheme.shapes.medium)
                                    )
                                }

                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = item.bodyPart?.stringValue ?: "",
                                            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                        )
                                        Text(
                                            text = if (item.prevLbs != null && item.prevReps != null) { "${item.prevLbs} lb (x${item.prevReps})" } else { stringResource(R.string.n_a) },
                                            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                        )
                                    }
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onBackground)
                            )
                        }
                    }
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            grouping.forEachIndexed { index, pair ->
                var count = 0

                for (i in 0 until index) {
                    count++
                    count += grouping[i].second.count()
                }

                Text(text = pair.first, fontSize = 15.sp, modifier = Modifier.clickable {
                    coroutineScope.launch {
                        listState.scrollToItem(count, 0)
                    }
                })
            }
        }
    }
}