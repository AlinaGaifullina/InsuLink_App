package ru.itis.domain.model

data class InsulinSensitivity(
    val id: String,
    val startTime: String,
    val endTime: String,
    val value: Float
)
