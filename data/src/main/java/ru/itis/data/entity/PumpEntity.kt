package ru.itis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pump")
data class PumpEntity(
    @PrimaryKey
    val id: String,
    val deviceId: String,
    val carbMeasurementUnit: String,
    val glucoseMeasurementUnit: String,
    val insulinActiveTime: String,
    val userId: String
)