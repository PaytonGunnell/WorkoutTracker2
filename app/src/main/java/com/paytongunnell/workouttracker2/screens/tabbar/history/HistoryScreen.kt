package com.paytongunnell.workouttracker2.screens.tabbar.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.paytongunnell.workouttracker2.R
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.ui.theme.blueTextButton
import com.paytongunnell.workouttracker2.utils.formatMillisToHoursAndMinutes
import com.paytongunnell.workouttracker2.utils.formatTime1
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    workouts: List<Workout>,
    onClickWorkout: (Workout) -> Unit,
    onRefreshHistory: () -> Unit,
    modifier: Modifier = Modifier
) {

    val grouping = workouts.groupBy {
        val localDate = Instant.ofEpochMilli(it.startTime)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        YearMonth.from(localDate)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Box {}
            Text(
                "Calendar",
                style = MaterialTheme.typography.blueTextButton
            )
        }

        LazyColumn(modifier = Modifier) {
            item {
                Row(modifier.fillMaxWidth()) {
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable {
                                onRefreshHistory() }
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    }
                }
            }

            grouping.forEach {
                item {
                    Text(
                        it.key.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())).uppercase(),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    it.value.forEach {
                        val totalVolume = it.exercises.flatMap { exercise -> exercise.sets.mapNotNull { set -> set.weight } }.sumOf { it ?: 0.0 }

                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .fillMaxWidth()
                                .border(
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable { onClickWorkout(it) }
                        ) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        it.name,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                                Text(
                                    formatTime1(it.startTime),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                )
                                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(painterResource(R.drawable.clock_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            formatMillisToHoursAndMinutes(it.endTime - it.startTime),
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(painterResource(R.drawable.weight_hanging_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            totalVolume.toString(),
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                                        Icon(painterResource(R.drawable.trophy_icon), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            "N/A",
                                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            "Exercise",
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            "Best Set",
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    it.exercises.forEach {
                                        val bestSet = it.sets.maxByOrNull { it.weight?.times((it.reps?.toDouble() ?: 0.0)) ?: 0.0 }

                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                "${it.sets.count()} x ${it.exerciseName}",
                                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                if (bestSet?.weight != null && bestSet?.reps != null) "${bestSet.weight} lb x ${bestSet.reps}" else "N/A",
                                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollTest() {
    var searchQuery by remember { mutableStateOf("") }

    // Scroll state for fading effect
    val scrollState = rememberLazyListState()

    // Item index at which fading out should start
    val fadeOutStartIndex = 10

    LazyColumn(
        state = scrollState,
        contentPadding = PaddingValues(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Sticky header with fading effect
        stickyHeader {
            FadingTopBar(
                scrollState = scrollState,
                onBackClick = { /* Handle back click */ },
                onStarClick = { /* Handle star click */ },
                onSearchQueryChange = { searchQuery = it },
                fadeOutStartIndex = fadeOutStartIndex
            )
        }

        // Items
        items(50) { index ->
            // Replace this with your actual content
            Text("Item $index", modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun FadingTopBar(
    scrollState: LazyListState,
    onBackClick: () -> Unit,
    onStarClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    fadeOutStartIndex: Int
) {
    // Calculate alpha based on scroll position and fade-out start index
    val alpha = calculateFadeOutAlpha(scrollState, fadeOutStartIndex)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue.copy(alpha = alpha))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.clickable(onClick = onBackClick))
        BasicTextField(
            value = "",
            onValueChange = { onSearchQueryChange(it) },
            textStyle = MaterialTheme.typography.titleSmall,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.clickable(onClick = onStarClick))
    }
}

/**
 * Calculate the alpha value for fading out based on scroll position and target item index.
 */
private fun calculateFadeOutAlpha(scrollState: LazyListState, fadeOutStartIndex: Int): Float {
    val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
    val firstVisibleItemOffset = scrollState.firstVisibleItemScrollOffset

    Log.d("scrolling", "${firstVisibleItemIndex}, ${firstVisibleItemOffset}")

    return if (firstVisibleItemIndex >= fadeOutStartIndex) {
        val scrollDistance = firstVisibleItemOffset
        val a = 1 - (scrollDistance / 1000f).coerceIn(0f, 1f)
        Log.d("scrolling", "a: $a")
        a
    } else {
        1f
    }
}
