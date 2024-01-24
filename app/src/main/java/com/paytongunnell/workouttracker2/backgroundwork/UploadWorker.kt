package com.paytongunnell.workouttracker2.backgroundwork

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}