package ru.itis.domain.repository

import ru.itis.domain.model.Basal

interface BasalRepository {

    suspend fun saveBasal(value: Basal)
    suspend fun updateBasal(value: Basal)

    suspend fun getBasalById(id: String): Basal?
    suspend fun getBasalByUserId(userId: String): List<Basal>
    suspend fun getAllBasal(): List<Basal>

    suspend fun deleteBasal(id: String)
    suspend fun deleteAllBasalForUser(userId: String)
}