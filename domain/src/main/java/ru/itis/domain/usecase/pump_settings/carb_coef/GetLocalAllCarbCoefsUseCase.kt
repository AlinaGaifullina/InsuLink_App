package ru.itis.domain.usecase.pump_settings.carb_coef

import ru.itis.domain.model.CarbCoef
import ru.itis.domain.repository.CarbCoefRepository
import javax.inject.Inject
import android.util.Log

class GetLocalAllCarbCoefsUseCase @Inject constructor(
    private val repository: CarbCoefRepository
) {

    suspend operator fun invoke(): List<CarbCoef> {
        return try {
            repository.getAllCarbCoefs()
        } catch (e: Exception) {
            Log.e("GetCarbCoefs", "Error loading carb coefficients", e)
            emptyList()
        }
    }
}