package com.paytongunnell.workouttracker2.di

import android.app.Application
import androidx.room.Room
import com.paytongunnell.workouttracker2.database.ExerciseDatabase
import com.paytongunnell.workouttracker2.network.ExerciseDBService
import com.paytongunnell.workouttracker2.network.FirebaseAuthClient
import com.paytongunnell.workouttracker2.network.WorkoutTrackerServerService
import com.paytongunnell.workouttracker2.repository.WorkoutTrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(application: Application): WorkoutTrackerRepository =
        WorkoutTrackerRepository(
            application,
            ExerciseDBService,
            FirebaseAuthClient,
            WorkoutTrackerServerService,
            Room.databaseBuilder(
                application,
                ExerciseDatabase::class.java,
                "exercise_database")
                    .fallbackToDestructiveMigration()
                    .build()
        )
}