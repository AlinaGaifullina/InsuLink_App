package ru.itis.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    // User ID
    val userId: Flow<String?>
    suspend fun setUserId(userId: String)

    // Pump ID
    val pumpId: Flow<String?>
    suspend fun setPumpId(pumpId: String)

    // Очистка
    suspend fun clearAll()
}