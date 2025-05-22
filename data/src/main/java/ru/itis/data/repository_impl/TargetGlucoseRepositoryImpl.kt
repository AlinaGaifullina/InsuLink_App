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

    override suspend fun saveGlucose(target: TargetGlucose) {
        dao.insert(target.toEntity())
    }

    override suspend fun updateGlucose(target: TargetGlucose) {
        dao.update(target.toEntity())
    }

    override suspend fun getGlucoseById(id: String): TargetGlucose? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getGlucoseByUserId(userId: String): List<TargetGlucose> {
        return dao.getByUserId(userId).map { it.toDomain() }
    }

    override suspend fun getAllGlucose(): List<TargetGlucose> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteGlucose(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteAllGlucoseForUser(userId: String) {
        dao.deleteAllByUserId(userId)
    }
}
