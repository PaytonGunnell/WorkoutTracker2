package com.paytongunnell.workouttracker2.utils

import java.util.Calendar

fun getWorkoutNameByTime(millis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
        hourOfDay in 5..11 -> "Early Morning Workout"
        hourOfDay in 12..17 -> "Afternoon Workout"
        hourOfDay in 18..23 -> "Evening Workout"
        else -> "Midnight Workout"
    }
}