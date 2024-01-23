package com.paytongunnell.workouttracker2.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatTime1(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val date = Date.from(instant)
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault())
    return formatter.format(instant.atZone(ZoneId.systemDefault()))
}

fun millisecondsToMinutesRounded(milliseconds: Long): Long {
    return (milliseconds + 30_000) / 60_000
}

fun formatMillisToHoursAndMinutes(millis: Long): String {
    val hours = millis / (1000 * 60 * 60)
    val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)

    return if (hours > 0) {
        String.format("%dh %02dm", hours, minutes)
    } else {
        String.format("%dm", minutes)
    }
}

fun formatMillisToDateTime(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val zoneId = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern("h:mm a, EEEE, MMM d yyyy", Locale.getDefault())
    return formatter.format(instant.atZone(zoneId))
}