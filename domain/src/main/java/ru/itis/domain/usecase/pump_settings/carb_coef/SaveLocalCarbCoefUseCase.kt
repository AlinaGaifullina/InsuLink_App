package ru.itis.domain.usecase.pump_settings.carb_coef

import android.util.Log
import ru.itis.domain.model.CarbCoef
import ru.itis.domain.repository.CarbCoefRepository
import javax.inject.Inject

class SaveLocalCarbCoefUseCase @Inject constructor(
    private val carbCoefRepository: CarbCoefRepository
) {

    suspend operator fun invoke(coef: CarbCoef) {
        try {
            carbCoefRepository.saveCarbCoef(coef)
        } catch (e: Exception) {
            Log.e("SaveLocalCarbCoefUseCase", "Error saving carb coef", e)
            throw CarbCoefSaveException("Failed to save carb coefficient", e)
        }
    }
}

class CarbCoefSaveException(message: String, cause: Throwable?) : Exception(message, cause)