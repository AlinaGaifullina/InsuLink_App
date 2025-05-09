package ru.itis.domain.repository

import ru.itis.domain.model.CarbCoef

interface CarbCoefRepository {
    suspend fun saveCarbCoef(coef: CarbCoef)
    suspend fun getAllCarbCoefs(): List<CarbCoef>
}