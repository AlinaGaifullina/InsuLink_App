package ru.itis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basal")
data class BasalEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val value: String,
)