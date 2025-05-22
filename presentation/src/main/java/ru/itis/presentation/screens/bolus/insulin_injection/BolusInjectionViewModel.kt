package ru.itis.presentation.screens.bolus.insulin_injection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.itis.domain.usecase.datastore.SaveIsInsulinInjectionInDataStoreUseCase
import javax.inject.Inject


data class BolusInjectionState(
    val isProcessActive: Boolean = true,
    val isProcessSuccess: Boolean = true,
    val bolusValue: Float = 0.0f,
)

sealed interface BolusInjectionEvent {
    object OnStopButtonClick : BolusInjectionEvent
    object OnOkButtonClick : BolusInjectionEvent
    object OnContinueInjectModalButtonClick : BolusInjectionEvent
    object OnStopModalButtonClick : BolusInjectionEvent
}

sealed interface BolusInjectionSideEffect {
    object NavigateProfile : BolusInjectionSideEffect
}

@HiltViewModel
class BolusInjectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveIsInsulinInjectionInDataStoreUseCase: SaveIsInsulinInjectionInDataStoreUseCase,
) : ViewModel() {


    private val initialBolusValue = savedStateHandle.get<Float>("bolusValue") ?: 0f

    private val _state: MutableStateFlow<BolusInjectionState> = MutableStateFlow(
        BolusInjectionState(
            bolusValue = initialBolusValue
        )
    )
    val state: StateFlow<BolusInjectionState> = _state

    private val _action = MutableSharedFlow<BolusInjectionSideEffect?>()
    val action: SharedFlow<BolusInjectionSideEffect?>
        get() = _action.asSharedFlow()

    init {
        viewModelScope.launch {
            saveIsInsulinInjectionInDataStoreUseCase(true)
            delay(10000)

            saveIsInsulinInjectionInDataStoreUseCase(false)

            _state.tryEmit(
                _state.value.copy(
                    isProcessSuccess = true,
                    isProcessActive = false
                )
            )
        }
    }

    fun event(bolusInjectionEvent: BolusInjectionEvent) {

        ViewModelProvider.NewInstanceFactory
        when (bolusInjectionEvent) {
            BolusInjectionEvent.OnStopButtonClick -> onStopButtonClick()
            BolusInjectionEvent.OnOkButtonClick -> onOkButtonClick()
            BolusInjectionEvent.OnContinueInjectModalButtonClick -> onContinueInjectModalButtonClick()
            BolusInjectionEvent.OnStopModalButtonClick -> onStopModalButtonClick()
        }
    }

    private fun onOkButtonClick() {
        viewModelScope.launch { _action.emit(BolusInjectionSideEffect.NavigateProfile) }
    }

    private fun onStopButtonClick() {

    }

    private fun onContinueInjectModalButtonClick() {

    }
    private fun onStopModalButtonClick() {
        viewModelScope.launch {
            saveIsInsulinInjectionInDataStoreUseCase(false)
        }
        _state.tryEmit(_state.value.copy(
            isProcessSuccess = false,
            isProcessActive = false)
        )
    }
}