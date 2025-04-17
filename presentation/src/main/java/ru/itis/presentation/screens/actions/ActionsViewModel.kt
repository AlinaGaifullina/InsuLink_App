package ru.itis.presentation.screens.actions

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
import javax.inject.Inject


data class ActionsState(
    val userPhone: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
)

sealed interface ActionsEvent {
}

sealed interface ActionsSideEffect {

}

@HiltViewModel
class ActionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<ActionsState> = MutableStateFlow(ActionsState())
    val state: StateFlow<ActionsState> = _state

    private val _action = MutableSharedFlow<ActionsSideEffect?>()
    val action: SharedFlow<ActionsSideEffect?>
        get() = _action.asSharedFlow()

    fun event(actionsEvent: ActionsEvent) {
        ViewModelProvider.NewInstanceFactory
//        when (actionsEvent) {
//
//        }
    }
}