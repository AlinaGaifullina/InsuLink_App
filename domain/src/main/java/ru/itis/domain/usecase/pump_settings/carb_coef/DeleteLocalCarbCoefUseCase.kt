package ru.itis.domain.usecase.pump_settings.carb_coef

import android.util.Log
import ru.itis.domain.repository.CarbCoefRepository
import javax.inject.Inject

class DeleteLocalCarbCoefUseCase @Inject constructor(
    private val carbCoefRepository: CarbCoefRepository
) {
    suspend operator fun invoke(id: String) {
        try {
            carbCoefRepository.deleteCarbCoef(id)
        } catch (e: Exception) {
            Log.e("DeleteLocalCarbCoef", "Error deleting carb coefficient with id: $id", e)
        }
    }
}