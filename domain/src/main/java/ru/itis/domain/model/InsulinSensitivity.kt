package ru.itis.domain.model

data class InsulinSensitivity(
    val id: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val value: Float
)
