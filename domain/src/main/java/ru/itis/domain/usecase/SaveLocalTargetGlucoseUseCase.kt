package ru.itis.domain.usecase

import android.util.Log
import ru.itis.domain.model.TargetGlucose
import ru.itis.domain.repository.TargetGlucoseRepository
import javax.inject.Inject

class SaveLocalTargetGlucoseUseCase @Inject constructor(
    private val repository: TargetGlucoseRepository
) {

    suspend operator fun invoke(target: TargetGlucose) {
        try {
            repository.saveTarget(target)
        } catch (e: Exception) {
            Log.e("SaveTargetGlucose", "Error saving target: ${target.id}", e)
            throw TargetGlucoseSaveException("Failed to save target glucose", e)
        }
    }
}

class TargetGlucoseSaveException(message: String, cause: Throwable?) :
    Exception(message, cause)