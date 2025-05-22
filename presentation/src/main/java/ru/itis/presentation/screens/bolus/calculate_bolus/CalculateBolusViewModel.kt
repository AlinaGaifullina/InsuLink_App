package ru.itis.presentation.screens.bolus.calculate_bolus

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.domain.model.CarbCoef
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.model.TargetGlucose
import ru.itis.domain.usecase.datastore.GetUserIdFromDataStoreUseCase
import ru.itis.domain.usecase.pump_settings.carb_coef.GetLocalAllCarbCoefsUseCase
import ru.itis.domain.usecase.pump_settings.insulin_sensitivity.GetLocalAllInsulinSensitivityUseCase
import ru.itis.domain.usecase.pump_settings.target_glucose.GetLocalAllTargetGlucoseUseCase
import ru.itis.presentation.utils.BolusCalculator
import ru.itis.presentation.utils.roundToOneDecimal
import java.util.Calendar
import javax.inject.Inject


data class CalculateBolusState(
    val nutritionValue: Float = 2.0f, // съеденная еда
    val glucoseValue: Float = 5.6f, // текущий сахар
    val insulinForNutrition: Float = 0.0f, // инсулин на еду
    val insulinForCorrection: Float = 0.0f, // инсулин на коррекцию
    val activeInsulin: Float = 0.0f, // активный инсулин
    val bolusValue: Float = 0.0f, // итоговая дозировка инсулина

    val listOfCarbCoefs: List<CarbCoef> = listOf(),
    val listOfInsSens: List<InsulinSensitivity> = listOf(),
    val listOfGlucose: List<TargetGlucose> = listOf(),

    val isLoading: Boolean = false
)

sealed interface CalculateBolusEvent {
    object OnContinueButtonClick : CalculateBolusEvent
    object OnCancelingButtonClick : CalculateBolusEvent
    object OnBackButtonClick : CalculateBolusEvent
}

sealed interface CalculateBolusSideEffect {
    object NavigateProfile : CalculateBolusSideEffect
    object NavigateEditCalculateBolus : CalculateBolusSideEffect
    object NavigateBack : CalculateBolusSideEffect
}

@HiltViewModel
class CalculateBolusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserIdFromDataStoreUseCase: GetUserIdFromDataStoreUseCase,

    private val getLocalAllCarbCoefsUseCase: GetLocalAllCarbCoefsUseCase,
    private val getLocalAllInsulinSensitivityUseCase: GetLocalAllInsulinSensitivityUseCase,
    private val getLocalAllTargetGlucoseUseCase: GetLocalAllTargetGlucoseUseCase,
) : ViewModel() {

    private val initialNutritionValue = savedStateHandle.get<Float>("nutritionValue") ?: 0f
    private val initialGlucoseValue = savedStateHandle.get<Float>("glucoseValue") ?: 0f

    private val _state: MutableStateFlow<CalculateBolusState> = MutableStateFlow(
        CalculateBolusState(
            nutritionValue = initialNutritionValue,
            glucoseValue = initialGlucoseValue
        )
    )
    val state: StateFlow<CalculateBolusState> = _state

    private val _action = MutableSharedFlow<CalculateBolusSideEffect?>()
    val action: SharedFlow<CalculateBolusSideEffect?>
        get() = _action.asSharedFlow()

    private var userId: String = ""

    init {
        viewModelScope.launch {
            loadUserId()
            loadPumpSettings()
        }
    }

    private suspend fun loadUserId() {
        userId = getUserIdFromDataStoreUseCase()
            ?: throw IllegalStateException("User not authenticated")
    }

    private fun loadPumpSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Устанавливаем isLoading в true перед началом загрузки данных
                withContext(Dispatchers.Main) {
                    _state.update { it.copy(isLoading = true) }
                }

                val carbCoefsList = getLocalAllCarbCoefsUseCase()
                val insulinSensitivityList = getLocalAllInsulinSensitivityUseCase()
                val targetGlucoseList = getLocalAllTargetGlucoseUseCase()

                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            listOfCarbCoefs = carbCoefsList,
                            listOfInsSens = insulinSensitivityList,
                            listOfGlucose = targetGlucoseList,
                            isLoading = false
                        )
                    }
                    calculateBolus()
                }
            } catch (e: Exception) {
                // Обработка ошибки
                withContext(Dispatchers.Main) {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun calculateBolus() {
        val calculator = BolusCalculator(
            listOfCarbCoefs = _state.value.listOfCarbCoefs,
            listOfInsSens = _state.value.listOfInsSens,
            listOfGlucose = _state.value.listOfGlucose
        )

        val result = calculator.calculate(
            currentTime = getCurrentTime(),
            currentGlucose = _state.value.glucoseValue,
            breadUnits = _state.value.nutritionValue,
            activeInsulin = 2f
        )

        val roundedFoodInsulin = result.foodInsulin.roundToOneDecimal()
        val roundedCorrection = result.correctionInsulin.roundToOneDecimal()
        val roundedBolus = result.recommendedDose.roundToOneDecimal()

        Log.d("BOLUS_CALC", "=== Результаты ===")
        Log.d("BOLUS_CALC", "Питание: ${result.foodInsulin}")
        Log.d("BOLUS_CALC", "Коррекция: ${result.correctionInsulin}")
        Log.d("BOLUS_CALC", "Болюс: ${result.recommendedDose}")
        Log.d("BOLUS_CALC", "Питание rounded: $roundedFoodInsulin")
        Log.d("BOLUS_CALC", "Коррекция rounded: $roundedCorrection")
        Log.d("BOLUS_CALC", "Болюс rounded: $roundedBolus")

        _state.update {
            it.copy(
                insulinForNutrition = roundedFoodInsulin,
                insulinForCorrection = roundedCorrection,
                bolusValue = roundedBolus
            )
        }
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }

    fun event(calculateBolusEvent: CalculateBolusEvent) {
        ViewModelProvider.NewInstanceFactory
        when (calculateBolusEvent) {
            CalculateBolusEvent.OnContinueButtonClick -> onContinueButtonClick()
            CalculateBolusEvent.OnCancelingButtonClick -> onCancelingButtonClick()
            CalculateBolusEvent.OnBackButtonClick -> onBackButtonClick()
        }
    }
    private fun onContinueButtonClick() {
        viewModelScope.launch { _action.emit(CalculateBolusSideEffect.NavigateEditCalculateBolus) }
    }

    private fun onCancelingButtonClick() {
        viewModelScope.launch { _action.emit(CalculateBolusSideEffect.NavigateProfile) }
    }
    private fun onBackButtonClick() {
        viewModelScope.launch { _action.emit(CalculateBolusSideEffect.NavigateBack) }
    }
}