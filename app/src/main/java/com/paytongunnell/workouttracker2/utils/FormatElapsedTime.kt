package com.paytongunnell.workouttracker2.utils

fun formatElapsedTime(timeInMillis: Long): String {
    val seconds = timeInMillis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun formatRestTime(timeInMillis: Long): String {
    val seconds = timeInMillis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}