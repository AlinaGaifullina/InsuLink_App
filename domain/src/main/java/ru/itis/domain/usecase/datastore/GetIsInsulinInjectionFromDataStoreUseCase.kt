package ru.itis.domain.usecase.datastore

import kotlinx.coroutines.flow.first
import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetIsInsulinInjectionFromDataStoreUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Boolean {
        return userPreferencesRepository.isInsulinInjection.first()
    }
}
