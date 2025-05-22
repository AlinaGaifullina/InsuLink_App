package ru.itis.domain.repository

import ru.itis.domain.model.TargetGlucose

interface TargetGlucoseRepository {

    suspend fun saveGlucose(target: TargetGlucose)
    suspend fun updateGlucose(target: TargetGlucose)

    suspend fun getGlucoseById(id: String): TargetGlucose?
    suspend fun getGlucoseByUserId(userId: String): List<TargetGlucose>
    suspend fun getAllGlucose(): List<TargetGlucose>

    suspend fun deleteGlucose(id: String)
    suspend fun deleteAllGlucoseForUser(userId: String)
}