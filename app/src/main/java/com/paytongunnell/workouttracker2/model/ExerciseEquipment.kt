package com.paytongunnell.workouttracker2.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: find icons/images for all equipment

@Serializable
enum class ExerciseEquipment(val stringValue: String) {
    @SerialName("assisted")
    ASSISTED("Assisted"),
    @SerialName("band")
    BAND("Band"),
    @SerialName("barbell")
    BARBELL("Barbell"),
    @SerialName("body weight")
    BODY_WEIGHT("Body weight"),
    @SerialName("bosu ball")
    BOSU_BALL("Bosu ball"),
    @SerialName("cable")
    CABLE("Cable"),
    @SerialName("dumbbell")
    DUMBBELL("Dumbbell"),
    @SerialName("elliptical machine")
    ELLIPTICAL_MACHINE("Elliptical machine"),
    @SerialName("ez barbell")
    EZ_BARBELL("Ez barbell"),
    @SerialName("hammer")
    HAMMER("Hammer"),
    @SerialName("kettlebell")
    KETTLEBELL("Kettlebell"),
    @SerialName("leverage machine")
    LEVERAGE_MACHINE("Leverage machine"),
    @SerialName("medicine ball")
    MEDICINE_BALL("Medicine ball"),
    @SerialName("olympic barbell")
    OLYMPIC_BARBELL("Olympic barbell"),
    @SerialName("resistance band")
    RESISTANCE_BAND("Resistance band"),
    @SerialName("roller")
    ROLLER("Roller"),
    @SerialName("rope")
    ROPE("Rope"),
    @SerialName("skierg machine")
    SKIERG_MACHINE("Skierg machine"),
    @SerialName("sled machine")
    SLED_MACHINE("Sled machine"),
    @SerialName("smith machine")
    SMITH_MACHINE("Smith machine"),
    @SerialName("stability ball")
    STABILITY_BALL("Stability ball"),
    @SerialName("stationary bike")
    STATIONARY_BIKE("Stationary bike"),
    @SerialName("stepmill machine")
    STEPMILL_MACHINE("Stepmill machine"),
    @SerialName("tire")
    TIRE("Tire"),
    @SerialName("trap bar")
    TRAP_BAR("Trap bar"),
    @SerialName("upper body ergometer")
    UPPER_BODY_ERGOMETER("Upper body ergometer"),
    @SerialName("weighted")
    WEIGHTED("Weighted"),
    @SerialName("wheel roller")
    WHEEL_ROLLER("Wheel roller")
}

