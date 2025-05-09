package ru.itis.domain.usecase.datastore

import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject


class SavePumpIdInDataStoreUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(pumpId: String) {
        require(pumpId.isNotBlank()) { "Pump ID cannot be blank" }
        repository.setPumpId(pumpId)
    }
}