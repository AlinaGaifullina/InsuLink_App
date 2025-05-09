package ru.itis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carb_coef")
data class CarbCoefEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val coef: String,
)