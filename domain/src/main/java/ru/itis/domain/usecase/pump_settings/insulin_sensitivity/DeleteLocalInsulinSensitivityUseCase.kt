package ru.itis.domain.usecase.pump_settings.insulin_sensitivity

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.repository.InsulinSensitivityRepository

class DeleteLocalInsulinSensitivityUseCase @Inject constructor(
    private val insulinSensitivityRepository: InsulinSensitivityRepository
) {
    suspend operator fun invoke(id: String) {
        try {
            insulinSensitivityRepository.deleteInsulinSensitivity(id)
        } catch (e: Exception) {
            Log.e("DeleteLocalInsulinSensitivity", "Error deleting insulin sensitivity with id: $id", e)
        }
    }
}
