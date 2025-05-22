package ru.itis.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    // User ID
    val userId: Flow<String?>
    suspend fun setUserId(userId: String)
    suspend fun deleteUserId()

    // Pump ID
    val pumpId: Flow<String?>
    suspend fun setPumpId(pumpId: String)

    // Insulin Injection Flag
    val isInsulinInjection: Flow<Boolean>
    suspend fun setIsInsulinInjection(isInjection: Boolean)

    // Очистка
    suspend fun clearAll()

}