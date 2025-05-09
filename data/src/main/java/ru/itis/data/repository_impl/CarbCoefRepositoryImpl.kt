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

    override suspend fun getAllCarbCoefs(): List<CarbCoef> {
        return dao.getAll().map { it.toDomain() }
    }
}