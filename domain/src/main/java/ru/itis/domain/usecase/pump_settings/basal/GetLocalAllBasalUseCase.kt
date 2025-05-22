package ru.itis.domain.usecase.pump_settings.basal

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.model.Basal
import ru.itis.domain.repository.BasalRepository

class GetLocalAllBasalUseCase @Inject constructor(
    private val basalRepository: BasalRepository
) {
    suspend operator fun invoke(): List<Basal> {
        return try {
            basalRepository.getAllBasal()
        } catch (e: Exception) {
            Log.e("GetLocalAllBasal", "Error loading all basal", e)
            emptyList()
        }
    }
}
