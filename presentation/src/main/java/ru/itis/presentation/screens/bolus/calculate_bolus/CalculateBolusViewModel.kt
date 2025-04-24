package ru.itis.presentation.screens.bolus.calculate_bolus

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


data class CalculateBolusState(
    val userPhone: String = "",
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
) : ViewModel() {

    private val _state: MutableStateFlow<CalculateBolusState> = MutableStateFlow(CalculateBolusState())
    val state: StateFlow<CalculateBolusState> = _state

    private val _action = MutableSharedFlow<CalculateBolusSideEffect?>()
    val action: SharedFlow<CalculateBolusSideEffect?>
        get() = _action.asSharedFlow()

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