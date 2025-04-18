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
import kotlinx.coroutines.launch
import ru.itis.domain.model.CarbCoef
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.model.TargetGlucose
import javax.inject.Inject


data class ProfileState(
    val name: String = "Иван",
    val surname: String = "Иванов",
    val patronymic: String = "Иванович",
    val birthDate: String = "",
    val isMale: Boolean? = null,
    val height: Float = 0.0f,
    val weight: Float = 0.0f,
    val insulin: String = "",
    val isProfileMode: Boolean = true,
    val isBreadUnits: Boolean = true,
    val isMmolLiter: Boolean = true,
    val pumpName: String = "",
    val isPumpConnected: Boolean = false,
    val activeInsulinTime: Float = 0.0f,
    // Carb
    val listOfCarbCoefs: List<CarbCoef> = listOf(
        CarbCoef("1",0,0,3,0,2.0f),
        CarbCoef("1",0,0,3,0,2.0f),
        CarbCoef("1",0,0,3,0,2.0f),
        CarbCoef("1",0,0,3,0,2.0f),
        CarbCoef("1",0,0,3,0,2.0f)
    ),
    val isCarbCoefExpanded: Boolean = false,
    val carbCoefExpandedItemIndex: Int? = null,
    // Sensitivity
    val listOfSensitivity: List<InsulinSensitivity> = listOf(),
    val isSensitivityExpanded: Boolean = false,
    val sensitivityExpandedItemIndex: Int? = null,
    // Glucose
    val listOfGlucose: List<TargetGlucose> = listOf(),
    val isGlucoseExpanded: Boolean = false,
    val glucoseExpandedItemIndex: Int? = null
)

sealed interface ProfileEvent {
    object OnEditProfileButtonClick : ProfileEvent
    object OnConnectButtonClick : ProfileEvent
    object OnModeChange : ProfileEvent
    object OnCarbUnitChange : ProfileEvent
    object OnGlucoseUnitChange : ProfileEvent
    object OnCarbCoefExpandedChange : ProfileEvent
    object OnSensitivityExpandedChange : ProfileEvent
    object OnGlucoseExpandedChange : ProfileEvent
    data class OnInsulinChange(val value: String) : ProfileEvent
    data class OnActiveInsulinChange(val value: Float) : ProfileEvent
    data class OnCarbCoefExpandedItemChange(val value: Int) : ProfileEvent
    data class OnSensitivityExpandedItemChange(val value: Int) : ProfileEvent
    data class OnGlucoseExpandedItemChange(val value: Int) : ProfileEvent
}

sealed interface ProfileSideEffect {
    object NavigateEditProfileScreen : ProfileSideEffect
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _action = MutableSharedFlow<ProfileSideEffect?>()
    val action: SharedFlow<ProfileSideEffect?>
        get() = _action.asSharedFlow()

    fun event(profileEvent: ProfileEvent) {
        ViewModelProvider.NewInstanceFactory
        when (profileEvent) {
            is ProfileEvent.OnInsulinChange -> onInsulinChange(profileEvent.value)
            is ProfileEvent.OnActiveInsulinChange -> onActiveInsulinChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefExpandedItemChange -> onCarbCoefExpandedItemChange(profileEvent.value)
            is ProfileEvent.OnSensitivityExpandedItemChange -> onSensitivityExpandedItemChange(profileEvent.value)
            is ProfileEvent.OnGlucoseExpandedItemChange -> onGlucoseExpandedItemChange(profileEvent.value)
            ProfileEvent.OnEditProfileButtonClick -> onEditProfileButtonClick()
            ProfileEvent.OnConnectButtonClick -> onConnectButtonClick()
            ProfileEvent.OnModeChange -> onModeChange()
            ProfileEvent.OnCarbCoefExpandedChange -> onCarbCoefExpandedChange()
            ProfileEvent.OnSensitivityExpandedChange -> onSensitivityExpandedChange()
            ProfileEvent.OnGlucoseExpandedChange -> onGlucoseExpandedChange()
            ProfileEvent.OnCarbUnitChange -> onCarbUnitChange()
            ProfileEvent.OnGlucoseUnitChange -> onGlucoseUnitChange()
        }
    }

    private fun onInsulinChange(insulin: String) {
        _state.tryEmit(_state.value.copy(insulin = insulin))
    }
    private fun onActiveInsulinChange(activeInsulin: Float) {
        _state.tryEmit(_state.value.copy(activeInsulinTime = activeInsulin))
    }

    private fun onCarbCoefExpandedItemChange(index: Int) {
        val isEmpty = _state.value.carbCoefExpandedItemIndex == null
        _state.tryEmit(_state.value.copy(
            carbCoefExpandedItemIndex = if(isEmpty){
                index
            } else {
                if (index == _state.value.carbCoefExpandedItemIndex){
                    null
                } else index
            }
        ))
    }

    private fun onSensitivityExpandedItemChange(index: Int) {
        val isEmpty = _state.value.sensitivityExpandedItemIndex == null
        _state.tryEmit(_state.value.copy(
            sensitivityExpandedItemIndex = if(isEmpty){
                index
            } else {
                if (index == _state.value.sensitivityExpandedItemIndex){
                    null
                } else index
            }
        ))
    }

    private fun onGlucoseExpandedItemChange(index: Int) {
        val isEmpty = _state.value.glucoseExpandedItemIndex == null
        _state.tryEmit(_state.value.copy(
            glucoseExpandedItemIndex = if(isEmpty){
                index
            } else {
                if (index == _state.value.glucoseExpandedItemIndex){
                    null
                } else index
            }
        ))
    }

    private fun onModeChange() {
        _state.tryEmit(_state.value.copy(isProfileMode = !_state.value.isProfileMode))
    }
    private fun onCarbCoefExpandedChange() {
        _state.tryEmit(_state.value.copy(
            isCarbCoefExpanded = !_state.value.isCarbCoefExpanded,
            carbCoefExpandedItemIndex = null
        ))
    }
    private fun onSensitivityExpandedChange() {
        _state.tryEmit(_state.value.copy(
            isSensitivityExpanded = !_state.value.isSensitivityExpanded,
            sensitivityExpandedItemIndex = null
        ))
    }
    private fun onGlucoseExpandedChange() {
        _state.tryEmit(_state.value.copy(
            isGlucoseExpanded = !_state.value.isGlucoseExpanded,
            glucoseExpandedItemIndex = null
        ))
    }
    private fun onCarbUnitChange() {
        _state.tryEmit(_state.value.copy(isBreadUnits = !_state.value.isBreadUnits))
    }

    private fun onGlucoseUnitChange() {
        _state.tryEmit(_state.value.copy(isMmolLiter = !_state.value.isMmolLiter))
    }

    private fun onEditProfileButtonClick() {
        viewModelScope.launch { _action.emit(ProfileSideEffect.NavigateEditProfileScreen) }
    }

    private fun onConnectButtonClick() {

    }
}