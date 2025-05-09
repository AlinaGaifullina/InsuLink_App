package ru.itis.domain.usecase

import android.util.Log
import ru.itis.domain.model.TargetGlucose
import ru.itis.domain.repository.TargetGlucoseRepository
import javax.inject.Inject

class GetLocalAllTargetGlucoseUseCase @Inject constructor(
    private val repository: TargetGlucoseRepository
) {

    suspend operator fun invoke(): List<TargetGlucose> {
        return try {
            repository.getAllTargets()
        } catch (e: Exception) {
            Log.e("GetTargets", "Loading failed", e)
            emptyList()
        }
    }
}