package ru.itis.domain.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val gender: String,
    val birthDate: String,
    val height: Float,
    val weight: Float,
    val insulin: String,
)