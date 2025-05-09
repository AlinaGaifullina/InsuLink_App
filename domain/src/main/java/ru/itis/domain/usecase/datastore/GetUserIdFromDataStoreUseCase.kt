package ru.itis.domain.usecase.datastore

import kotlinx.coroutines.flow.first
import ru.itis.domain.repository.UserPreferencesRepository
import javax.inject.Inject


class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): String? {
        return userPreferencesRepository.userId.first()
    }
}