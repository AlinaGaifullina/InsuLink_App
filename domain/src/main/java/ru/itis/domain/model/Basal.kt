package ru.itis.domain.model

data class Basal(
    val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val value: Float
)