package ru.itis.domain.usecase.pump_settings.basal

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.model.Basal
import ru.itis.domain.repository.BasalRepository

class SaveLocalBasalUseCase @Inject constructor(
    private val basalRepository: BasalRepository
) {
    suspend operator fun invoke(value: Basal) {
        try {
            basalRepository.saveBasal(value)
        } catch (e: Exception) {
            Log.e("SaveLocalBasal", "Error saving basal", e)
        }
    }
}
