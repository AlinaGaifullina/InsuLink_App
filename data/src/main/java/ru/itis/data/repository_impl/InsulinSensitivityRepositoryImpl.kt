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

    override suspend fun getAllInsulinSensitivity(): List< InsulinSensitivity> {
        return dao.getAll().map { it.toDomain() }
    }
}