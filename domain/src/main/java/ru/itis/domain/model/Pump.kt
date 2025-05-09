package ru.itis.domain.model

data class Pump(
    val id: String,
    val deviceId: String,
    val carbMeasurementUnit: String,
    val glucoseMeasurementUnit: String,
    val insulinActiveTime: Float,
    val userId: String
)
