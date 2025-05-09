package ru.itis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "target_glucose")
data class TargetGlucoseEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val startValue: String,
    val endValue: String
)