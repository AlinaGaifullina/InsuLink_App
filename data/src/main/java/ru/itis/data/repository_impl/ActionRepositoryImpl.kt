package ru.itis.data.repository_impl

import ru.itis.data.dao.ActionDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.Action
import ru.itis.domain.repository.ActionRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ActionRepositoryImpl @Inject constructor(
    private val dao: ActionDao
) : ActionRepository {

    override suspend fun saveAction(action: Action) {
        dao.insert(action.toEntity())
    }

    override suspend fun getAllActions(): List<Action> {
        return dao.getAll().map { it.toDomain() }
    }

    override suspend fun getActionsByDateRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Action> {
        return dao.getByDateRange(
            start.toString(),
            end.toString()
        ).map { it.toDomain() }
    }
}