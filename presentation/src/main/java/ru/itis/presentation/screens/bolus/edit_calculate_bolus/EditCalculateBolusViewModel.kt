package ru.itis.presentation.screens.bolus.edit_calculate_bolus

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


data class EditCalculateBolusState(
    val bolusValue: Float = 0.0f,
)

sealed interface EditCalculateBolusEvent {
    object OnInjectButtonClick : EditCalculateBolusEvent
    object OnCancelingButtonClick : EditCalculateBolusEvent
    object OnBackButtonClick : EditCalculateBolusEvent
    data class OnBolusValueChange(val value: Float) : EditCalculateBolusEvent
}

sealed interface EditCalculateBolusSideEffect {
    object NavigateProfile : EditCalculateBolusSideEffect
    object NavigateBolusInjection : EditCalculateBolusSideEffect
    object NavigateBack : EditCalculateBolusSideEffect
}

@HiltViewModel
class EditCalculateBolusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<EditCalculateBolusState> = MutableStateFlow(EditCalculateBolusState())
    val state: StateFlow<EditCalculateBolusState> = _state

    private val _action = MutableSharedFlow<EditCalculateBolusSideEffect?>()
    val action: SharedFlow<EditCalculateBolusSideEffect?>
        get() = _action.asSharedFlow()

    fun event(editCalculateBolusEvent: EditCalculateBolusEvent) {
        ViewModelProvider.NewInstanceFactory
        when (editCalculateBolusEvent) {
            EditCalculateBolusEvent.OnInjectButtonClick -> onInjectButtonClick()
            EditCalculateBolusEvent.OnCancelingButtonClick -> onCancelingButtonClick()
            EditCalculateBolusEvent.OnBackButtonClick -> onBackButtonClick()
            is EditCalculateBolusEvent.OnBolusValueChange -> onBolusValueChange(editCalculateBolusEvent.value)
        }
    }
    private fun onInjectButtonClick() {
        viewModelScope.launch { _action.emit(EditCalculateBolusSideEffect.NavigateBolusInjection) }
    }

    private fun onCancelingButtonClick() {
        viewModelScope.launch { _action.emit(EditCalculateBolusSideEffect.NavigateProfile) }
    }
    private fun onBackButtonClick() {
        viewModelScope.launch { _action.emit(EditCalculateBolusSideEffect.NavigateBack) }
    }

    private fun onBolusValueChange(bolusValue: Float) {
        _state.tryEmit(_state.value.copy(bolusValue = bolusValue))
    }
}