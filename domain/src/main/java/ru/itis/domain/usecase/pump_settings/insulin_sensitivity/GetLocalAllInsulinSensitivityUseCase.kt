package ru.itis.domain.usecase.pump_settings.insulin_sensitivity

import android.util.Log
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.repository.InsulinSensitivityRepository
import javax.inject.Inject

class GetLocalAllInsulinSensitivityUseCase @Inject constructor(
    private val repository: InsulinSensitivityRepository
) {

    suspend operator fun invoke(): List<InsulinSensitivity> {
        return try {
            repository.getAllInsulinSensitivity()
        } catch (e: Exception) {
            Log.e("GetSensitivity", "Loading failed", e)
            emptyList()
        }
    }
}