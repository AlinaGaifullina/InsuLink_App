package ru.itis.domain.repository

import ru.itis.domain.model.TargetGlucose

interface TargetGlucoseRepository {
    suspend fun saveTarget(target: TargetGlucose)
    suspend fun getAllTargets(): List<TargetGlucose>
}