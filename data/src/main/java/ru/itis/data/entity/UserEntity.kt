package ru.itis.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val gender: String,
    val birthDate: String,
    val height: String,
    val weight: String,
    val insulin: String,
)