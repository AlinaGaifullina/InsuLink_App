package ru.itis.domain.usecase.pump_settings.basal

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.repository.BasalRepository

class DeleteLocalBasalUseCase @Inject constructor(
    private val basalRepository: BasalRepository
) {
    suspend operator fun invoke(id: String) {
        try {
            basalRepository.deleteBasal(id)
        } catch (e: Exception) {
            Log.e("DeleteLocalBasal", "Error deleting basal with id: $id", e)
        }
    }
}

