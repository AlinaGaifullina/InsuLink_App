package ru.itis.domain.repository

import ru.itis.domain.model.InsulinSensitivity


interface InsulinSensitivityRepository {
    suspend fun saveInsulinSensitivity(value: InsulinSensitivity)
    suspend fun getAllInsulinSensitivity(): List<InsulinSensitivity>
}