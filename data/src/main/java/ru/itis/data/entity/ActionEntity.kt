package ru.itis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_action")
data class ActionEntity(
    @PrimaryKey
    val id: String,
    val date: String,
    val type: String,
    val sugar: String,
    val food: String,
    val bolus: String,
    val basalPercent: String,
    val basalHours: String,
    val basalMinutes: String,
    val basalSeconds: String
)