package ru.itis.domain.usecase.datastore

import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject


class SaveIsInsulinInjectionInDataStoreUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(value: Boolean) {
        repository.setIsInsulinInjection(value)
    }
}