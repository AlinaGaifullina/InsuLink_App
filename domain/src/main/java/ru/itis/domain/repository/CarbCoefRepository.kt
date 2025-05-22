package ru.itis.domain.repository

import ru.itis.domain.model.CarbCoef

interface CarbCoefRepository {

    suspend fun saveCarbCoef(coef: CarbCoef)
    suspend fun updateCarbCoef(coef: CarbCoef)

    suspend fun getCarbCoefById(id: String): CarbCoef?
    suspend fun getCarbCoefByUserId(userId: String): List<CarbCoef>
    suspend fun getAllCarbCoefs(): List<CarbCoef>

    suspend fun deleteCarbCoef(id: String)
    suspend fun deleteAllCarbCoefsForUser(userId: String)
}