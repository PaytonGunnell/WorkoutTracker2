package com.paytongunnell.workouttracker2.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchTimer {
    var onTick: (Long) -> Unit = {}

    private var timerScope: CoroutineScope? = null
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L

    fun start() {
        if (timerScope == null) {
            timerScope = CoroutineScope(Dispatchers.Default)
            startTime = System.currentTimeMillis() - elapsedTime

            timerScope?.launch {
                while (true) {
                    Log.d("stopwatch", "${startTime}")
                    elapsedTime = System.currentTimeMillis() - startTime
                    onTick(elapsedTime)
                    delay(1000) // Update every second
                }
            }
        } else {
            println("Timer is already running.")
        }
    }

    fun changeStartTime(changeTo: Long) {
        startTime = changeTo
    }

    fun pause() {
        timerScope?.cancel()
        timerScope = null
    }

    fun stop() {
        timerScope?.cancel()
        timerScope = null
        elapsedTime = 0L
    }
}