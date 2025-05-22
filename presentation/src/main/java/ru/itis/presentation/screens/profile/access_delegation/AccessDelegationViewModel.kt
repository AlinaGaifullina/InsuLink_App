package ru.itis.presentation.screens.profile.access_delegation

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
import javax.inject.Inject


data class AccessDelegationState(
    val emailValue: String = "",
    val isAccountSearching: Boolean = false,
    val listOfAccounts: List<String> = listOf()
)

sealed interface AccessDelegationEvent {
    object OnBackButtonClick : AccessDelegationEvent
    object OnGrantAccessButtonClick : AccessDelegationEvent
    object OnConfirmButtonClick : AccessDelegationEvent
    data class OnTrashButtonClick(val value: Int) : AccessDelegationEvent
    data class OnEmailValueChange(val value: String) : AccessDelegationEvent
}

sealed interface AccessDelegationSideEffect {
    object NavigateProfile : AccessDelegationSideEffect
}

@HiltViewModel
class AccessDelegationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,

) : ViewModel() {

    private val _state: MutableStateFlow<AccessDelegationState> = MutableStateFlow(AccessDelegationState())
    val state: StateFlow<AccessDelegationState> = _state

    private val _action = MutableSharedFlow<AccessDelegationSideEffect?>()
    val action: SharedFlow<AccessDelegationSideEffect?>
        get() = _action.asSharedFlow()

    init {
    }

    fun event(accessDelegationEvent: AccessDelegationEvent) {

        ViewModelProvider.NewInstanceFactory
        when (accessDelegationEvent) {
            AccessDelegationEvent.OnBackButtonClick -> onBackButtonClick()
            AccessDelegationEvent.OnConfirmButtonClick -> onConfirmButtonClick()
            AccessDelegationEvent.OnGrantAccessButtonClick-> onGrantAccessButtonClick()
            is AccessDelegationEvent.OnEmailValueChange -> onEmailValueChange(accessDelegationEvent.value)
            is AccessDelegationEvent.OnTrashButtonClick-> onTrashButtonClick(accessDelegationEvent.value)
        }
    }

    private fun onBackButtonClick() {
        viewModelScope.launch { _action.emit(AccessDelegationSideEffect.NavigateProfile) }
    }

    private fun onGrantAccessButtonClick() {
        viewModelScope.launch {
            _state.tryEmit(_state.value.copy(isAccountSearching = true))

            delay(1000)

            _state.tryEmit(
                _state.value.copy(
                    isAccountSearching = false
                )
            )
        }
    }

    private fun onConfirmButtonClick() {
        val newListOfAccounts = _state.value.listOfAccounts.toMutableList()
        newListOfAccounts.add(_state.value.emailValue)
        _state.tryEmit(_state.value.copy(
            listOfAccounts = newListOfAccounts,
            emailValue = "",
        ))
    }

    private fun onTrashButtonClick(index: Int) {
        val newListOfAccounts = _state.value.listOfAccounts.toMutableList()
        newListOfAccounts.removeAt(index)
        _state.tryEmit(_state.value.copy(
            listOfAccounts = newListOfAccounts,
        ))
    }

    private fun onEmailValueChange(value: String) {
        _state.tryEmit(_state.value.copy(
            emailValue = value,
        ))
    }
}