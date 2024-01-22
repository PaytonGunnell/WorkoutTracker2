package com.paytongunnell.workouttracker2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MuscleGroup(val stringValue: String) {
    @SerialName("abductors")
    ABDUCTORS("Abductors"),
    @SerialName("abs")
    ABS("Abs"),
    @SerialName("adductors")
    ADDUCTORS("Adductors"),
    @SerialName("biceps")
    BICEPS("Biceps"),
    @SerialName("calves")
    CALVES("Calves"),
    @SerialName("cardiovascular system")
    CARDIOVASCULAR_SYSTEM("Cardiovascular system"),
    @SerialName("delts")
    DELTS("Delts"),
    @SerialName("forearms")
    FOREARMS("Forearms"),
    @SerialName("glutes")
    GLUTES("Glutes"),
    @SerialName("hamstrings")
    HAMSTRINGS("Hamstrings"),
    @SerialName("lats")
    LATS("Lats"),
    @SerialName("levator scapulae")
    LEVATOR_SCAPULAE("Levator scapulae"),
    @SerialName("pectorals")
    PECTORALS("Pectorals"),
    @SerialName("quads")
    QUADS("Quads"),
    @SerialName("serratus anterior")
    SERRATUS_ANTERIOR("Serratus anterior"),
    @SerialName("spine")
    SPINE("Spine"),
    @SerialName("traps")
    TRAPS("Traps"),
    @SerialName("triceps")
    TRICEPS("Triceps"),
    @SerialName("upper back")
    UPPER_BACK("Upper back")
}
