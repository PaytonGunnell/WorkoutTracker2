package com.paytongunnell.workouttracker2.screens.tabbar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TabBarViewModel @Inject constructor(
    application: Application,
    private val repository: WorkoutTrackerRepository
): AndroidViewModel(application) {


}