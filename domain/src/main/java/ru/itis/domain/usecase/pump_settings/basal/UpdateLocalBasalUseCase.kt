package ru.itis.domain.usecase.pump_settings.basal

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.model.Basal
import ru.itis.domain.repository.BasalRepository

class UpdateLocalBasalUseCase @Inject constructor(
    private val basalRepository: BasalRepository
) {
    suspend operator fun invoke(value: Basal) {
        try {
            basalRepository.updateBasal(value)
        } catch (e: Exception) {
            Log.e("UpdateLocalBasal", "Error updating basal", e)
        }
    }
}
