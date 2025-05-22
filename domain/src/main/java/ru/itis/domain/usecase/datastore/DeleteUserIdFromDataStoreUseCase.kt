package ru.itis.domain.usecase.datastore

import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class DeleteUserIdFromDataStoreUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        userPreferencesRepository.deleteUserId()
    }
}