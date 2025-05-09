package ru.itis.domain.repository

import ru.itis.domain.model.Pump

interface PumpRepository {
    suspend fun savePump(pump: Pump)
    suspend fun getPump(pumpId: String): Pump?
    suspend fun getUserPumps(userId: String): List<Pump>
    suspend fun deletePump(pumpId: String)
}