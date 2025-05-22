package ru.itis.data.repository_impl

import ru.itis.data.dao.CarbCoefDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.CarbCoef
import ru.itis.domain.repository.CarbCoefRepository
import javax.inject.Inject


class CarbCoefRepositoryImpl @Inject constructor(
    private val dao: CarbCoefDao
) : CarbCoefRepository {

    override suspend fun saveCarbCoef(coef: CarbCoef) {
        dao.insert(coef.toEntity())
    }

    override suspend fun updateCarbCoef(coef: CarbCoef) {
        dao.update(coef.toEntity())
    }

    override suspend fun getCarbCoefById(id: String): CarbCoef? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getCarbCoefByUserId(userId: String): List<CarbCoef> {
        return dao.getByUserId(userId).map { it.toDomain() }
    }

    override suspend fun getAllCarbCoefs(): List<CarbCoef> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteCarbCoef(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteAllCarbCoefsForUser (userId: String) {
        dao.deleteAllByUserId(userId)
    }
}
