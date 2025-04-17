package ru.itis.presentation.screens.auth.greeting

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


data class GreetingState(
    val userPhone: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
)

sealed interface GreetingEvent {
    object OnFillProfileButtonClick : GreetingEvent
    object OnSkipButtonCLick : GreetingEvent
}

sealed interface GreetingSideEffect {
    object NavigateFillProfile : GreetingSideEffect
    object NavigateProfile : GreetingSideEffect
}

@HiltViewModel
class GreetingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<GreetingState> = MutableStateFlow(GreetingState())
    val state: StateFlow<GreetingState> = _state

    private val _action = MutableSharedFlow<GreetingSideEffect?>()
    val action: SharedFlow<GreetingSideEffect?>
        get() = _action.asSharedFlow()

    fun event(greetingEvent: GreetingEvent) {
        ViewModelProvider.NewInstanceFactory
        when (greetingEvent) {
            GreetingEvent.OnSkipButtonCLick -> onSkipButtonCLick()
            GreetingEvent.OnFillProfileButtonClick -> onFillProfileButtonClick()
        }
    }
    private fun onSkipButtonCLick() {
        viewModelScope.launch { _action.emit(GreetingSideEffect.NavigateProfile) }
    }

    private fun onFillProfileButtonClick() {
        viewModelScope.launch { _action.emit(GreetingSideEffect.NavigateFillProfile) }
    }
}