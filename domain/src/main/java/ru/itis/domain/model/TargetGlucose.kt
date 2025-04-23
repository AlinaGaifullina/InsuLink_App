package ru.itis.domain.model

data class TargetGlucose(
    val id: String,
    val startTime: String,
    val endTime: String,
    val startValue: Float,
    val endValue: Float
)
