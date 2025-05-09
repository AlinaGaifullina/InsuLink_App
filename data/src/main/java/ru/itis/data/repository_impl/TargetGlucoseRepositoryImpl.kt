package ru.itis.data.repository_impl

import ru.itis.data.dao.TargetGlucoseDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.TargetGlucose
import ru.itis.domain.repository.TargetGlucoseRepository
import javax.inject.Inject

class TargetGlucoseRepositoryImpl @Inject constructor(
    private val dao: TargetGlucoseDao
) : TargetGlucoseRepository {

    override suspend fun saveTarget(target: TargetGlucose) {
        dao.insert(target.toEntity())
    }

    override suspend fun getAllTargets(): List<TargetGlucose> {
        return dao.getAll().map { it.toDomain() }
    }
}