package ru.itis.domain.repository

import ru.itis.domain.model.InsulinSensitivity


interface InsulinSensitivityRepository {

    suspend fun saveInsulinSensitivity(value: InsulinSensitivity)
    suspend fun updateInsulinSensitivity(value: InsulinSensitivity)

    suspend fun getInsulinSensitivityById(id: String): InsulinSensitivity?
    suspend fun getInsulinSensitivityByUserId(userId: String): List<InsulinSensitivity>
    suspend fun getAllInsulinSensitivity(): List<InsulinSensitivity>

    suspend fun deleteInsulinSensitivity(id: String)
    suspend fun deleteAllInsulinSensitivityForUser(userId: String)
}