package com.paytongunnell.workouttracker2.datasync

import android.app.Application
import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import com.paytongunnell.workouttracker2.utils.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConnectivityReceiver(): BroadcastReceiver() {

    @Inject
    lateinit var repository: WorkoutTrackerRepository

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("connreceiver", "onReceive:called")
        if (isNetworkConnected(context)) {
            Log.d("connreceiver", "onReceive:networkConnectedTrue")


            // check repository for data marked for syncing and sync it
            if (::repository.isInitialized) {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    repository.syncFirebaseAfterBackOnline()
                }
            }
        }
    }
}

//class SyncWorker(
//    context: Context,
//    params: WorkerParameters
//) : Worker(context, params) {
//
//    @Inject
//    lateinit var repository: WorkoutTrackerRepository
//
//
//    override fun doWork(): Result {
//        val c = CoroutineScope(Dispatchers.IO)
//        // Perform the work, e.g., sync data to Firebase
//        repository.syncFirebaseAfterBackOnline()
//
//        // Indicate success
//        return Result.success()
//    }
//}