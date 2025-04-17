package ru.itis.presentation.screens.bolus

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


data class BolusState(
    val userPhone: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
)

sealed interface BolusEvent {
}

sealed interface BolusSideEffect {

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
//        when (profileEvent) {
//
//        }
    }
}