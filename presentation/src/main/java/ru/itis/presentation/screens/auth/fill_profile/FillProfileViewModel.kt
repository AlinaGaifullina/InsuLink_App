package ru.itis.presentation.screens.auth.fill_profile

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


data class FillProfileState(
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val birthDate: String = "",
    val isMale: Boolean = true,
)

sealed interface FillProfileEvent {
    object OnNextButtonClick : FillProfileEvent
    data class OnNameChange(val value: String) : FillProfileEvent
    data class OnSurnameChange(val value: String) : FillProfileEvent
    data class OnPatronymicChange(val value: String) : FillProfileEvent
    data class OnBirthDateChange(val value: String) : FillProfileEvent
    object OnGenderChange : FillProfileEvent
}

sealed interface FillProfileSideEffect {
    object NavigateFillHealth : FillProfileSideEffect
}

@HiltViewModel
class FillProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<FillProfileState> = MutableStateFlow(FillProfileState())
    val state: StateFlow<FillProfileState> = _state

    private val _action = MutableSharedFlow<FillProfileSideEffect?>()
    val action: SharedFlow<FillProfileSideEffect?>
        get() = _action.asSharedFlow()

    fun event(fillProfileEvent: FillProfileEvent) {
        ViewModelProvider.NewInstanceFactory
        when (fillProfileEvent) {
            is FillProfileEvent.OnNameChange -> onNameChange(fillProfileEvent.value)
            is FillProfileEvent.OnSurnameChange -> onSurnameChange(fillProfileEvent.value)
            is FillProfileEvent.OnPatronymicChange -> onPatronymicChange(fillProfileEvent.value)
            is FillProfileEvent.OnBirthDateChange -> onBirthDateChange(fillProfileEvent.value)
            FillProfileEvent.OnGenderChange -> onGenderChange()
            FillProfileEvent.OnNextButtonClick -> onNextButtonClick()
        }
    }

    private fun onNameChange(name: String) {
        _state.tryEmit(_state.value.copy(name = name))
    }

    private fun onSurnameChange(surname: String) {
        _state.tryEmit(_state.value.copy(surname = surname))
    }

    private fun onPatronymicChange(patronymic: String) {
        _state.tryEmit(_state.value.copy(patronymic = patronymic))
    }

    private fun onBirthDateChange(birthDate: String) {
        _state.tryEmit(_state.value.copy(birthDate = birthDate))
    }

    private fun onGenderChange() {
        _state.tryEmit(_state.value.copy(isMale = !_state.value.isMale))
    }

    private fun onNextButtonClick() {
        viewModelScope.launch { _action.emit(FillProfileSideEffect.NavigateFillHealth) }
    }
}