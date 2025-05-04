package ru.itis.presentation.screens.bolus.bolus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BolusState(
    val isBreadUnits: Boolean = true,
    val isMmolLiter: Boolean = true,
    val nutritionValue: Float = 2.0f,
    val glucoseValue: Float = 5.6f,

)

sealed interface BolusEvent {
    object OnCalculateButtonClick : BolusEvent
    object OnCancelingButtonClick : BolusEvent
    data class OnNutritionValueChange(val value: Float) : BolusEvent
    data class OnGlucoseValueChange(val value: Float) : BolusEvent
}

sealed interface BolusSideEffect {
    object NavigateCalculateBolus : BolusSideEffect
    object NavigateBack : BolusSideEffect
}

@HiltViewModel
class BolusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<BolusState> = MutableStateFlow(BolusState())
    val state: StateFlow<BolusState> = _state

    private val _action = MutableSharedFlow<BolusSideEffect?>()
    val action: SharedFlow<BolusSideEffect?>
        get() = _action.asSharedFlow()

    fun event(bolusEvent: BolusEvent) {
        ViewModelProvider.NewInstanceFactory
        when (bolusEvent) {
            BolusEvent.OnCalculateButtonClick -> onCalculateButtonClick()
            BolusEvent.OnCancelingButtonClick -> onCancelingButtonClick()
            is BolusEvent.OnGlucoseValueChange -> onGlucoseValueChange(bolusEvent.value)
            is BolusEvent.OnNutritionValueChange -> onNutritionValueChange(bolusEvent.value)
        }
    }

    private fun onGlucoseValueChange(glucoseValue: Float) {
        _state.tryEmit(_state.value.copy(glucoseValue = glucoseValue))
    }

    private fun onNutritionValueChange(nutritionValue: Float) {
        _state.tryEmit(_state.value.copy(nutritionValue = nutritionValue))
    }

    private fun onCalculateButtonClick() {
        viewModelScope.launch { _action.emit(BolusSideEffect.NavigateCalculateBolus) }
    }

    private fun onCancelingButtonClick() {
        viewModelScope.launch { _action.emit(BolusSideEffect.NavigateBack) }
    }
}