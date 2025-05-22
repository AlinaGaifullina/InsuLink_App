package ru.itis.data.repository_impl

import ru.itis.data.dao.InsulinSensitivityDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.repository.InsulinSensitivityRepository
import javax.inject.Inject


class InsulinSensitivityRepositoryImpl @Inject constructor(
    private val dao: InsulinSensitivityDao
) : InsulinSensitivityRepository {

    override suspend fun saveInsulinSensitivity(value: InsulinSensitivity) {
        dao.insert(value.toEntity())
    }

    override suspend fun updateInsulinSensitivity(value: InsulinSensitivity) {
        dao.update(value.toEntity())
    }

    override suspend fun getInsulinSensitivityById(id: String): InsulinSensitivity? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getInsulinSensitivityByUserId(userId: String): List<InsulinSensitivity> {
        return dao.getByUserId(userId).map { it.toDomain() }
    }

    override suspend fun getAllInsulinSensitivity(): List<InsulinSensitivity> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteInsulinSensitivity(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteAllInsulinSensitivityForUser (userId: String) {
        dao.deleteAllByUserId(userId)
    }
}
