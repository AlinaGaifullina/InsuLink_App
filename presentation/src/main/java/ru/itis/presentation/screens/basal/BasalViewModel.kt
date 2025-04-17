package ru.itis.presentation.screens.basal

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


data class BasalState(
    val userPhone: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
)

sealed interface BasalEvent {
}

sealed interface BasalSideEffect {

}

@HiltViewModel
class BasalViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<BasalState> = MutableStateFlow(BasalState())
    val state: StateFlow<BasalState> = _state

    private val _action = MutableSharedFlow<BasalSideEffect?>()
    val action: SharedFlow<BasalSideEffect?>
        get() = _action.asSharedFlow()

    fun event(basalEvent: BasalEvent) {
        ViewModelProvider.NewInstanceFactory
//        when (profileEvent) {
//
//        }
    }
}