package ru.itis.domain.model

data class TargetGlucose(
    val id: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val startValue: Float,
    val endValue: Float
)
