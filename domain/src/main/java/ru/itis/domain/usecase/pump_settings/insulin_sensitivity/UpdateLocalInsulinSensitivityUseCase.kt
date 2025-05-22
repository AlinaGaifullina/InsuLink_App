package ru.itis.domain.usecase.pump_settings.insulin_sensitivity

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.repository.InsulinSensitivityRepository

class UpdateLocalInsulinSensitivityUseCase @Inject constructor(
    private val insulinSensitivityRepository: InsulinSensitivityRepository
) {
    suspend operator fun invoke(value: InsulinSensitivity) {
        try {
            insulinSensitivityRepository.updateInsulinSensitivity(value)
        } catch (e: Exception) {
            Log.e("UpdateLocalInsulinSensitivity", "Error updating insulin sensitivity", e)
        }
    }
}
