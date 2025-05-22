package ru.itis.data.repository_impl

import ru.itis.data.dao.BasalDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.Basal
import ru.itis.domain.repository.BasalRepository
import javax.inject.Inject


class BasalRepositoryImpl @Inject constructor(
    private val dao: BasalDao
) : BasalRepository {

    override suspend fun saveBasal(value: Basal) {
        dao.insert(value.toEntity())
    }

    override suspend fun updateBasal(value: Basal) {
        dao.update(value.toEntity())
    }

    override suspend fun getBasalById(id: String): Basal? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getBasalByUserId(userId: String): List<Basal>{
        return dao.getByUserId(userId).map { it.toDomain() }
    }

    override suspend fun getAllBasal(): List<Basal> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun deleteBasal(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteAllBasalForUser (userId: String) {
        dao.deleteAllByUserId(userId)
    }
}
