package ru.itis.domain.repository

import ru.itis.domain.model.Action
import java.time.LocalDateTime

interface ActionRepository {
    suspend fun saveAction(action: Action)
    suspend fun getAllActions(): List<Action>
    suspend fun getActionsByDateRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): List<Action>
}