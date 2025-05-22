package ru.itis.presentation.utils

import ru.itis.domain.model.CarbCoef
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.model.TargetGlucose

class BolusCalculator(
    private val listOfCarbCoefs: List<CarbCoef>,
    private val listOfInsSens: List<InsulinSensitivity>,
    private val listOfGlucose: List<TargetGlucose>
) {
    data class InsulinCalculationResult(
        val recommendedDose: Float,
        val foodInsulin: Float,
        val correctionInsulin: Float,
        val error: String? = null
    )

    fun calculate(
        currentTime: String,
        currentGlucose: Float,
        breadUnits: Float,
        activeInsulin: Float
    ): InsulinCalculationResult {
        return try {
            println("Текущие коэффициенты:")
            println("Углеводные: $listOfCarbCoefs")
            println("Чувствительности: $listOfInsSens")
            println("Целевая глюкоза: $listOfGlucose")
            // 1. Получаем текущий углеводный коэффициент
            val currentCarbCoef = getCurrentCarbCoef(currentTime)
                ?: return InsulinCalculationResult(
                    0f, 0f, 0f,
                    "Не найден углеводный коэффициент для времени $currentTime"
                )

            // 2. Получаем текущий коэффициент чувствительности
            val currentInsulinSensitivity = getCurrentInsulinSensitivity(currentTime)
                ?: return InsulinCalculationResult(
                    0f, 0f, 0f,
                    "Не найден коэффициент чувствительности для времени $currentTime"
                )

            if (currentInsulinSensitivity == 0f) {
                return InsulinCalculationResult(
                    0f, 0f, 0f,
                    "Коэффициент чувствительности не может быть равен нулю"
                )
            }

            // 3. Рассчитываем целевую глюкозу
            val targetGlucose = calculateTargetGlucose(currentTime, currentGlucose)

            // 4. Выполняем расчеты
            val foodInsulin = breadUnits * currentCarbCoef
            val correctionInsulin = (currentGlucose - targetGlucose) / currentInsulinSensitivity
            val recommendedDose = foodInsulin + correctionInsulin - activeInsulin

            InsulinCalculationResult(
                recommendedDose = recommendedDose,
                foodInsulin = foodInsulin,
                correctionInsulin = correctionInsulin
            )
        } catch (e: Exception) {
            InsulinCalculationResult(
                0f, 0f, 0f,
                "Ошибка расчета: ${e.localizedMessage}"
            )
        }
    }

    fun getCurrentCarbCoef(currentTime: String): Float? {
        return listOfCarbCoefs
            .firstOrNull { isTimeInInterval(currentTime, it.startTime, it.endTime) }
            ?.coef
    }

    fun getCurrentInsulinSensitivity(currentTime: String): Float? {
        return listOfInsSens
            .firstOrNull { isTimeInInterval(currentTime, it.startTime, it.endTime) }
            ?.value
    }

    fun calculateTargetGlucose(currentTime: String, currentGlucose: Float): Float {
        val targetRange = listOfGlucose
            .firstOrNull { isTimeInInterval(currentTime, it.startTime, it.endTime) }
            ?: return currentGlucose

        return when {
            currentGlucose < targetRange.startValue -> targetRange.startValue
            currentGlucose > targetRange.endValue -> targetRange.endValue
            else -> currentGlucose
        }
    }

    private fun isTimeInInterval(currentTime: String, startTime: String, endTime: String): Boolean {
        val (currentH, currentM) = parseTime(currentTime)
        val (startH, startM) = parseTime(startTime)
        val (endH, endM) = parseTime(endTime)

        val currentTotal = currentH * 60 + currentM
        val startTotal = startH * 60 + startM
        val endTotal = endH * 60 + endM

        return if (startTotal < endTotal) {
            currentTotal in startTotal until endTotal
        } else {
            currentTotal >= startTotal || currentTotal < endTotal
        }
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val parts = time.split(":")
        return if (parts.size == 2) {
            parts[0].toInt() to parts[1].toInt()
        } else {
            0 to 0
        }
    }
}