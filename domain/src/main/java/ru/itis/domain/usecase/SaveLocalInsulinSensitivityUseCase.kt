package ru.itis.domain.usecase

import android.util.Log
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.repository.InsulinSensitivityRepository
import javax.inject.Inject

class SaveLocalInsulinSensitivityUseCase @Inject constructor(
    private val repository: InsulinSensitivityRepository
) {

    suspend operator fun invoke(value: InsulinSensitivity) {
        try {
            repository.saveInsulinSensitivity(value)
        } catch (e: Exception) {
            Log.e("SaveInsulinSensitivity", "Error saving value: ${value.id}", e)
            throw InsulinSensitivitySaveException("Failed to save insulin sensitivity", e)
        }
    }
}

class InsulinSensitivitySaveException(message: String, cause: Throwable?) :
    Exception(message, cause)