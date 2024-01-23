package com.paytongunnell.workouttracker2.utils

import androidx.room.TypeConverter
import com.paytongunnell.workouttracker2.model.ExerciseBlock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExerciseBlockTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromJson(jsonString: String?): List<ExerciseBlock>? {
        if (jsonString == null) {
            return null
        }
        return json.decodeFromString<List<ExerciseBlock>>(jsonString)
    }

    @TypeConverter
    fun toJson(value: List<ExerciseBlock>?): String? {
        if (value == null) {
            return null
        }
        return json.encodeToString(value)
    }
}