package ru.itis.data.repository_impl

import ru.itis.data.dao.PumpDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.Pump
import ru.itis.domain.repository.PumpRepository
import javax.inject.Inject

class PumpRepositoryImpl @Inject constructor(
    private val dao: PumpDao
) : PumpRepository {

    override suspend fun savePump(pump: Pump) {
        val existing = dao.getById(pump.id)
        if (existing != null) {
            dao.update(pump.toEntity())
        } else {
            dao.insert(pump.toEntity())
        }
    }

    override suspend fun getPump(pumpId: String): Pump? {
        return dao.getById(pumpId)?.toDomain()
    }

    override suspend fun getUserPumps(userId: String): List<Pump> {
        return dao.getByUserId(userId)
            .map { it.toDomain() }
    }

    override suspend fun deletePump(pumpId: String) {
        dao.delete(pumpId)
    }
}