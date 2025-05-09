package ru.itis.domain.usecase

import android.util.Log
import ru.itis.domain.model.Pump
import ru.itis.domain.repository.PumpRepository
import javax.inject.Inject

class SaveLocalPumpUseCase @Inject constructor(
    private val repository: PumpRepository
) {
    suspend operator fun invoke(pump: Pump) {
        try {
            repository.savePump(pump)
        } catch (e: Exception) {
            Log.e("SaveInsulinSensitivity", "Error saving value: ${pump.id}", e)
        }
    }
}