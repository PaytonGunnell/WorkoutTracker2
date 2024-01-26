package com.paytongunnell.workouttracker2.utils

enum class UploadTo(val stringValue: String) {
    FIREBASE("toFirebase"),
    LOCAL("toLocal");

    companion object {
        // Function to deserialize a string to UploadTo
        fun fromString(value: String): UploadTo? {
            return values().find { it.stringValue == value }
        }
    }
}

enum class UploadType(val stringValue: String) {
    EXERCISE("exercises"),
    WORKOUT("workouts");

    companion object {
        // Function to deserialize a string to UploadType
        fun fromString(value: String): UploadType? {
            return values().find { it.stringValue == value }
        }
    }
}