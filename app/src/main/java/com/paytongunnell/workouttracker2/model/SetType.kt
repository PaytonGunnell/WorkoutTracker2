package com.paytongunnell.workouttracker2.model

import kotlinx.serialization.Serializable

@Serializable
enum class SetType(val stringValue: String) {
    NORMAL("Normal"),
    WARMUP("Warm up"),
    DROP("Drop"),
    FAILURE("Failure")
}