package com.paytongunnell.workouttracker2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.paytongunnell.workouttracker2.utils.StringListTypeConverter
import com.paytongunnell.workouttracker2.model.Exercise
import com.paytongunnell.workouttracker2.model.Workout
import com.paytongunnell.workouttracker2.utils.ExerciseBlockTypeConverter
import com.paytongunnell.workouttracker2.utils.HashMapStringSetTypeConverter
import com.paytongunnell.workouttracker2.utils.SetIdSetHashmapSericalizer

@Database(entities = [Exercise::class, Workout::class], version = 5, exportSchema = false)
@TypeConverters(StringListTypeConverter::class, HashMapStringSetTypeConverter::class, ExerciseBlockTypeConverter::class)
abstract class ExerciseDatabase: RoomDatabase() {

    abstract val exerciseDao: ExerciseDao
    abstract val workoutDao: WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getInstance(context: Context): ExerciseDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExerciseDatabase::class.java,
                        "wger_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}