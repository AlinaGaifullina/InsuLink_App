package ru.itis.presentation.screens.profile

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


data class ProfileState(
    val userPhone: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
)

sealed interface ProfileEvent {
}

sealed interface HomeSideEffect {
    data class NavigateToDishDetailsScreen(val id: Int) : HomeSideEffect
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _action = MutableSharedFlow<HomeSideEffect?>()
    val action: SharedFlow<HomeSideEffect?>
        get() = _action.asSharedFlow()

    fun event(profileEvent: ProfileEvent) {
        ViewModelProvider.NewInstanceFactory
//        when (profileEvent) {
//
//        }
    }
}