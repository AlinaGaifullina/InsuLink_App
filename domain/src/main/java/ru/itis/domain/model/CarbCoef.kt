package ru.itis.domain.model

data class CarbCoef(
    val id: String,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val coef: Float
)
