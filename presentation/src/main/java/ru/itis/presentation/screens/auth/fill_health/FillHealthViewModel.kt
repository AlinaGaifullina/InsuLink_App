package ru.itis.presentation.screens.auth.fill_health

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

data class FillHealthState(
    val height: Float = 0.0f,
    val weight: Float = 0.0f,
    val insulin: String = "",
)

sealed interface FillHealthEvent {
    object OnDoneButtonClick : FillHealthEvent
    data class OnHeightChange(val value: Float) : FillHealthEvent
    data class OnWeightChange(val value: Float) : FillHealthEvent
    data class OnInsulinChange(val value: String) : FillHealthEvent
}

sealed interface FillHealthSideEffect {
    object NavigateProfile : FillHealthSideEffect
}

@HiltViewModel
class FillHealthViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<FillHealthState> = MutableStateFlow(FillHealthState())
    val state: StateFlow<FillHealthState> = _state

    private val _action = MutableSharedFlow<FillHealthSideEffect?>()
    val action: SharedFlow<FillHealthSideEffect?>
        get() = _action.asSharedFlow()

    fun event(fillHealthEvent: FillHealthEvent) {
        ViewModelProvider.NewInstanceFactory
        when (fillHealthEvent) {
            is FillHealthEvent.OnHeightChange -> onHeightChange(fillHealthEvent.value)
            is FillHealthEvent.OnWeightChange -> onWeightChange(fillHealthEvent.value)
            is FillHealthEvent.OnInsulinChange -> onInsulinChange(fillHealthEvent.value)
            FillHealthEvent.OnDoneButtonClick -> onDoneButtonClick()
        }
    }

    private fun onHeightChange(height: Float) {
        _state.tryEmit(_state.value.copy(height = height))
    }

    private fun onWeightChange(weight: Float) {
        _state.tryEmit(_state.value.copy(weight = weight))
    }

    private fun onInsulinChange(insulin: String) {
        _state.tryEmit(_state.value.copy(insulin = insulin))
    }

    private fun onDoneButtonClick() {
        viewModelScope.launch { _action.emit(FillHealthSideEffect.NavigateProfile) }
    }
}