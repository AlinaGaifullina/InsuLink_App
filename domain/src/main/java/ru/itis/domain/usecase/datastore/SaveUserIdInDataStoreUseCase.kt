package ru.itis.domain.usecase.datastore

import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class SaveUserIdInDataStoreUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(userId: String) {
        repository.setUserId(userId)
    }
}