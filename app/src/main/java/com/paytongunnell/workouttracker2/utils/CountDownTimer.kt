package com.paytongunnell.workouttracker2.utils

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue

class CountDownTimer(
    private var count: Long = 50000,
    private var speed: Int = 1,
    originalCount: Long = count
) : CountDownTimer(count / speed, 1000L / speed) {

    private var _timerSeconds  by mutableLongStateOf(count)
    val timerSeconds: Long
        get() = _timerSeconds

    var timerCount = count
    val originalTimerCount = originalCount

    var resetTimer = false

    fun changeCount(count: Long) {
        this.count = count
    }


    override fun onTick(millisUntilFinished: Long) {
        Log.d("pause", "onTick: ${millisUntilFinished}")
        timerCount = millisUntilFinished * speed
        _timerSeconds = millisUntilFinished * speed
    }

    override fun onFinish() {
        resetTimer = true
        Log.d("onFinish", "count: ${count}")
        Log.d("onFinish", "seconds: ${_timerSeconds}")
        Log.d("onFinish", "speed: ${speed}")
    }
}