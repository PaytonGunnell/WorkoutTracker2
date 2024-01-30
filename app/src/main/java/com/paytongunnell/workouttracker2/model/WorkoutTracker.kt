package com.paytongunnell.workouttracker2.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

data class WorkoutTracker(
    val uId: String? = null,
    val workout: Workout = Workout(userId = uId ?: "none"),
    val elapsedMillis: Long = 0L,
    val isRestTimerRunning: Boolean = false,
    val automaticTiming: Boolean = true,
) {

//    val elapsedMillis: Long
//        get() = workout.endTime - workout.startTime
}