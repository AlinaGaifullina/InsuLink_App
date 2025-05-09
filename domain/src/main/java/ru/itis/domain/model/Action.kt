package ru.itis.domain.model

import java.time.LocalDateTime

data class Action(
    val id: String,
    val date: LocalDateTime,
    val type: ActionType,
    val sugar: Float,
    val food: Float,
    val bolus: Float,
    val basalPercent: Int,
    val basalHours: Int,
    val basalMinutes: Int,
    val basalSeconds: Int
)
