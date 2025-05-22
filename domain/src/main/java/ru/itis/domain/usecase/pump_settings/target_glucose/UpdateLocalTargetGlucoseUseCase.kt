package ru.itis.domain.usecase.pump_settings.target_glucose

import javax.inject.Inject
import android.util.Log
import ru.itis.domain.model.TargetGlucose
import ru.itis.domain.repository.TargetGlucoseRepository

class UpdateLocalTargetGlucoseUseCase @Inject constructor(
    private val targetGlucoseRepository: TargetGlucoseRepository
) {
    suspend operator fun invoke(target: TargetGlucose) {
        try {
            targetGlucoseRepository.updateGlucose(target)
        } catch (e: Exception) {
            Log.e("UpdateLocalTargetGlucose", "Error updating target glucose", e)
        }
    }
}
