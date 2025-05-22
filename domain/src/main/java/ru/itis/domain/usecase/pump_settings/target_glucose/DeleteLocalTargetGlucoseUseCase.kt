package ru.itis.domain.usecase.pump_settings.target_glucose

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.repository.TargetGlucoseRepository

class DeleteLocalTargetGlucoseUseCase @Inject constructor(
    private val targetGlucoseRepository: TargetGlucoseRepository
) {
    suspend operator fun invoke(id: String) {
        try {
            targetGlucoseRepository.deleteGlucose(id)
        } catch (e: Exception) {
            Log.e("DeleteLocalTargetGlucose", "Error deleting target glucose with id: $id", e)
        }
    }
}
