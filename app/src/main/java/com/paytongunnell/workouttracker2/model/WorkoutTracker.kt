package com.paytongunnell.workouttracker2.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

data class WorkoutTracker(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Workout",
    val note: String = "",
//    val duration: Long,
//    val startTime: Long,
//    val endTime: Long,
//    val stopWatch: StopwatchTimer,
    var exercises: MutableList<ExerciseBlock> = mutableListOf()
) {
//    var exercises: MutableList<TrackerExercise> = mutableListOf()
//        private set

    //    fun changeStartTime(changeTo: Long) {
//        stopWatch.changeStartTime(changeTo)
//    }
    fun changeName(to: String): WorkoutTracker {
        return this.copy(name = to)
    }
    fun changeNote(to: String): WorkoutTracker {
        return this.copy(note = to)
    }
    fun addExercise(exercise: ExerciseBlock): WorkoutTracker {
        return this.copy(exercises = (exercises + exercise).toMutableList())
    }
    fun changeExercisePosition(index1: Int, index2: Int): WorkoutTracker {
        if (index1 < 0 || index1 >= exercises.size || index2 < 0 || index2 >= exercises.size) {
            throw IndexOutOfBoundsException("Invalid indices")
        }
        val list = exercises.toMutableList()
        val temp = list[index1]
        list[index1] = list[index2]
        list[index2] = temp

        return this.copy(exercises = list)
    }
}

class WorkoutTimer() {

    var timerScope: CoroutineScope? = null
    var startTime: Long = 0L

    fun start() {
        if (timerScope == null) {
            timerScope = CoroutineScope(Dispatchers.Default)
            startTime = System.currentTimeMillis()

            timerScope?.launch {
                while (true) {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    println("Elapsed Time: ${formatElapsedTime(elapsedTime)}")
                    delay(1000) // Update every second
                }
            }
        } else {
            println("Timer is already running.")
        }
    }

    fun stop() {
        timerScope?.cancel()
        timerScope = null
        println("Timer stopped.")
    }

    fun getElapsedTime(): String {
        val currentTime = if (timerScope != null) {
            System.currentTimeMillis()
        } else {
            startTime
        }
        val elapsedTime = currentTime - startTime
        return formatElapsedTime(elapsedTime)
    }

    private fun formatElapsedTime(timeInMillis: Long): String {
        val seconds = timeInMillis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

}