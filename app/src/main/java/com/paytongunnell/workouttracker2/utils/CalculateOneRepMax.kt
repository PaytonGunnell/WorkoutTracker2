package com.paytongunnell.workouttracker2.utils

fun calculateOneRepMax(weight: Double, reps: Int): Int {
    if (reps <= 0) {
        throw IllegalArgumentException("Reps must be greater than 0")
    }

    return (weight * (1.0 + reps / 30.0)).toInt()
}