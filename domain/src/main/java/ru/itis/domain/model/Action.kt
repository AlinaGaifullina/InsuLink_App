package ru.itis.domain.model

import java.sql.Date

data class Action(
    val id: String,
    val date: Date,
    val type: ActionType,
    val sugar: Float,
    val food: Int,
    val bolus: Float,
    val basalPercent: Int,
    val basalHours: Int,
    val basalMinutes: Int,
    val basalSeconds: Int
)
