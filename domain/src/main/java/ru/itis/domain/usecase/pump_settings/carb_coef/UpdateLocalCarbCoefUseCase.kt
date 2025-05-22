package ru.itis.domain.usecase.pump_settings.carb_coef

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.model.CarbCoef
import ru.itis.domain.repository.CarbCoefRepository

class UpdateLocalCarbCoefUseCase @Inject constructor(
    private val carbCoefRepository: CarbCoefRepository
) {
    suspend operator fun invoke(coef: CarbCoef) {
        try {
            carbCoefRepository.updateCarbCoef(coef)
        } catch (e: Exception) {
            Log.e("UpdateLocalCarbCoef", "Error updating carb coefficient", e)
        }
    }
}
