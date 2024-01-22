package com.paytongunnell.workouttracker2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BodyPart(val stringValue: String) {
    @SerialName("back")
    BACK("Back"),
    @SerialName("cardio")
    CARDIO("Cardio"),
    @SerialName("chest")
    CHEST("Chest"),
    @SerialName("lower arms")
    LOWER_ARMS("Lower arms"),
    @SerialName("lower legs")
    LOWER_LEGS("Lower legs"),
    @SerialName("neck")
    NECK("Neck"),
    @SerialName("shoulders")
    SHOULDERS("Shoulders"),
    @SerialName("upper arms")
    UPPER_ARMS("Upper arms"),
    @SerialName("upper legs")
    UPPER_LEGS("Upper legs"),
    @SerialName("waist")
    WAIST("Waist")
}