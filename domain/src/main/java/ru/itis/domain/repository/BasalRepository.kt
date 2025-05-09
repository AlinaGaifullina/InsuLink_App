package ru.itis.domain.repository

import ru.itis.domain.model.Basal

interface BasalRepository {
    suspend fun saveBasal(value: Basal)
    suspend fun getAllBasal(): List<Basal>
}