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
        CarbCoef("1","00:00","04:00",2.0f),
        CarbCoef("1","00:00","04:00",2.0f),
        CarbCoef("1","00:00","04:00",2.0f),
        CarbCoef("1","00:00","04:00",2.0f),
        CarbCoef("1","00:00","04:00",2.0f),
        CarbCoef("1","00:00","04:00",2.0f),
    ),
    val isCarbCoefExpanded: Boolean = false,
    val carbCoefExpandedItemIndex: Int? = null,
    val carbCoefNewStartTime: String = "00:00",
    val carbCoefNewEndTime: String = "03:00",
    val carbCoefNewValue: Float = 0.0f,
    val carbCoefItemStartTime: String = "00:00",
    val carbCoefItemEndTime: String = "03:00",
    val carbCoefItemValue: Float = 0.0f,
    // Sensitivity
    val listOfInsSens: List<InsulinSensitivity> = listOf(
        InsulinSensitivity("1","00:00","05:00",2.0f),
        InsulinSensitivity("1","00:00","05:00",2.0f),
        InsulinSensitivity("1","00:00","05:00",2.0f),
        InsulinSensitivity("1","00:00","05:00",2.0f),
        InsulinSensitivity("1","00:00","05:00",2.0f),
        InsulinSensitivity("1","00:00","05:00",2.0f),
    ),
    val isInsSensExpanded: Boolean = false,
    val insSensExpandedItemIndex: Int? = null,
    val insSensNewStartTime: String = "00:00",
    val insSensNewEndTime: String = "03:00",
    val insSensNewValue: Float = 0.0f,
    val insSensItemStartTime: String = "00:00",
    val insSensItemEndTime: String = "03:00",
    val insSensItemValue: Float = 0.0f,
    // Glucose
    val listOfGlucose: List<TargetGlucose> = listOf(
        TargetGlucose("1","00:00","05:00",2.0f, 5.0f),
        TargetGlucose("1","00:00","05:00",2.0f, 5.0f),
        TargetGlucose("1","00:00","05:00",2.0f, 5.0f),
        TargetGlucose("1","00:00","05:00",2.0f, 5.0f),
        TargetGlucose("1","00:00","05:00",2.0f, 5.0f),
    ),
    val isGlucoseExpanded: Boolean = false,
    val glucoseExpandedItemIndex: Int? = null,
    val glucoseNewStartTime: String = "00:00",
    val glucoseNewEndTime: String = "03:00",
    val glucoseNewStartValue: Float = 0.0f,
    val glucoseNewEndValue: Float = 0.0f,
    val glucoseItemStartTime: String = "00:00",
    val glucoseItemEndTime: String = "03:00",
    val glucoseItemStartValue: Float = 0.0f,
    val glucoseItemEndValue: Float = 0.0f,
)

sealed interface ProfileEvent {
    object OnEditProfileButtonClick : ProfileEvent
    object OnConnectButtonClick : ProfileEvent
    object OnModeChange : ProfileEvent
    object OnCarbUnitChange : ProfileEvent
    object OnGlucoseUnitChange : ProfileEvent
    data class OnInsulinChange(val value: String) : ProfileEvent
    data class OnActiveInsulinChange(val value: Float) : ProfileEvent
    // Carb
    object OnCarbCoefExpandedChange : ProfileEvent
    data class OnCarbCoefExpandedItemChange(val value: Int) : ProfileEvent
    data class OnCarbCoefNewValueChange(val value: Float) : ProfileEvent
    data class OnCarbCoefNewStartTimeChange(val value: String) : ProfileEvent
    data class OnCarbCoefNewEndTimeChange(val value: String) : ProfileEvent
    data class OnCarbCoefItemValueChange(val value: Float) : ProfileEvent
    data class OnCarbCoefItemStartTimeChange(val value: String) : ProfileEvent
    data class OnCarbCoefItemEndTimeChange(val value: String) : ProfileEvent
    data class OnSaveCarbCoefItem(val value: Int) : ProfileEvent
    data class OnDeleteCarbCoefItem(val value: Int) : ProfileEvent
    object OnAddCarbCoefItem : ProfileEvent
    // Sensitivity
    object OnInsSensExpandedChange : ProfileEvent
    data class OnInsSensExpandedItemChange(val value: Int) : ProfileEvent
    data class OnInsSensNewValueChange(val value: Float) : ProfileEvent
    data class OnInsSensNewStartTimeChange(val value: String) : ProfileEvent
    data class OnInsSensNewEndTimeChange(val value: String) : ProfileEvent
    data class OnInsSensItemValueChange(val value: Float) : ProfileEvent
    data class OnInsSensItemStartTimeChange(val value: String) : ProfileEvent
    data class OnInsSensItemEndTimeChange(val value: String) : ProfileEvent
    data class OnSaveInsSensItem(val value: Int) : ProfileEvent
    data class OnDeleteInsSensItem(val value: Int) : ProfileEvent
    object OnAddInsSensItem : ProfileEvent
    // Glucose
    object OnGlucoseExpandedChange : ProfileEvent
    data class OnGlucoseExpandedItemChange(val value: Int) : ProfileEvent
    data class OnGlucoseNewStartValueChange(val value: Float) : ProfileEvent
    data class OnGlucoseNewEndValueChange(val value: Float) : ProfileEvent
    data class OnGlucoseNewStartTimeChange(val value: String) : ProfileEvent
    data class OnGlucoseNewEndTimeChange(val value: String) : ProfileEvent
    data class OnGlucoseItemStartValueChange(val value: Float) : ProfileEvent
    data class OnGlucoseItemEndValueChange(val value: Float) : ProfileEvent
    data class OnGlucoseItemStartTimeChange(val value: String) : ProfileEvent
    data class OnGlucoseItemEndTimeChange(val value: String) : ProfileEvent
    data class OnSaveGlucoseItem(val value: Int) : ProfileEvent
    data class OnDeleteGlucoseItem(val value: Int) : ProfileEvent
    object OnAddGlucoseItem : ProfileEvent
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
            ProfileEvent.OnEditProfileButtonClick -> onEditProfileButtonClick()
            ProfileEvent.OnConnectButtonClick -> onConnectButtonClick()
            ProfileEvent.OnModeChange -> onModeChange()
            ProfileEvent.OnCarbCoefExpandedChange -> onCarbCoefExpandedChange()
            ProfileEvent.OnInsSensExpandedChange -> onInsSensExpandedChange()
            ProfileEvent.OnGlucoseExpandedChange -> onGlucoseExpandedChange()
            ProfileEvent.OnCarbUnitChange -> onCarbUnitChange()
            ProfileEvent.OnGlucoseUnitChange -> onGlucoseUnitChange()
            is ProfileEvent.OnInsulinChange -> onInsulinChange(profileEvent.value)
            is ProfileEvent.OnActiveInsulinChange -> onActiveInsulinChange(profileEvent.value)
            // Carb
            is ProfileEvent.OnCarbCoefExpandedItemChange -> onCarbCoefExpandedItemChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefNewValueChange -> onCarbCoefNewValueChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefNewStartTimeChange -> onCarbCoefNewStartTimeChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefNewEndTimeChange -> onCarbCoefNewEndTimeChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefItemValueChange -> onCarbCoefItemValueChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefItemStartTimeChange -> onCarbCoefItemStartTimeChange(profileEvent.value)
            is ProfileEvent.OnCarbCoefItemEndTimeChange -> onCarbCoefItemEndTimeChange(profileEvent.value)
            is ProfileEvent.OnSaveCarbCoefItem -> onSaveCarbCoefItem(profileEvent.value)
            is ProfileEvent.OnDeleteCarbCoefItem -> onDeleteCarbCoefItem(profileEvent.value)
            ProfileEvent.OnAddCarbCoefItem -> onAddCarbCoefItem()
            // Sensitivity
            is ProfileEvent.OnInsSensExpandedItemChange -> onInsSensExpandedItemChange(profileEvent.value)
            is ProfileEvent.OnInsSensNewValueChange -> onInsSensNewValueChange(profileEvent.value)
            is ProfileEvent.OnInsSensNewStartTimeChange -> onInsSensNewStartTimeChange(profileEvent.value)
            is ProfileEvent.OnInsSensNewEndTimeChange -> onInsSensNewEndTimeChange(profileEvent.value)
            is ProfileEvent.OnInsSensItemValueChange -> onInsSensItemValueChange(profileEvent.value)
            is ProfileEvent.OnInsSensItemStartTimeChange -> onInsSensItemStartTimeChange(profileEvent.value)
            is ProfileEvent.OnInsSensItemEndTimeChange -> onInsSensItemEndTimeChange(profileEvent.value)
            is ProfileEvent.OnSaveInsSensItem -> onSaveInsSensItem(profileEvent.value)
            is ProfileEvent.OnDeleteInsSensItem -> onDeleteInsSensItem(profileEvent.value)
            ProfileEvent.OnAddInsSensItem -> onAddInsSensItem()
            // Glucose
            is ProfileEvent.OnGlucoseExpandedItemChange -> onGlucoseExpandedItemChange(profileEvent.value)
            is ProfileEvent.OnGlucoseNewStartValueChange -> onGlucoseNewStartValueChange(profileEvent.value)
            is ProfileEvent.OnGlucoseNewEndValueChange -> onGlucoseNewEndValueChange(profileEvent.value)
            is ProfileEvent.OnGlucoseNewStartTimeChange -> onGlucoseNewStartTimeChange(profileEvent.value)
            is ProfileEvent.OnGlucoseNewEndTimeChange -> onGlucoseNewEndTimeChange(profileEvent.value)
            is ProfileEvent.OnGlucoseItemStartValueChange -> onGlucoseItemStartValueChange(profileEvent.value)
            is ProfileEvent.OnGlucoseItemEndValueChange -> onGlucoseItemEndValueChange(profileEvent.value)
            is ProfileEvent.OnGlucoseItemStartTimeChange -> onGlucoseItemStartTimeChange(profileEvent.value)
            is ProfileEvent.OnGlucoseItemEndTimeChange -> onGlucoseItemEndTimeChange(profileEvent.value)
            is ProfileEvent.OnSaveGlucoseItem -> onSaveGlucoseItem(profileEvent.value)
            is ProfileEvent.OnDeleteGlucoseItem -> onDeleteGlucoseItem(profileEvent.value)
            ProfileEvent.OnAddGlucoseItem -> onAddGlucoseItem()
        }
    }

    private fun onInsulinChange(insulin: String) {
        _state.tryEmit(_state.value.copy(insulin = insulin))
    }
    private fun onActiveInsulinChange(activeInsulin: Float) {
        _state.tryEmit(_state.value.copy(activeInsulinTime = activeInsulin))
    }

    // Carb

    private fun onCarbCoefExpandedItemChange(index: Int) {
        val isEmpty = _state.value.carbCoefExpandedItemIndex == null
        if(isEmpty){
            _state.tryEmit(_state.value.copy(
                carbCoefExpandedItemIndex = index,
                carbCoefItemValue = _state.value.listOfCarbCoefs[index].coef,
                carbCoefItemStartTime = _state.value.listOfCarbCoefs[index].startTime,
                carbCoefItemEndTime = _state.value.listOfCarbCoefs[index].endTime,
            ))
        } else {
            _state.tryEmit(_state.value.copy(
                carbCoefExpandedItemIndex = if (index == _state.value.carbCoefExpandedItemIndex){
                    null
                } else index,
                carbCoefItemValue = _state.value.listOfCarbCoefs[index].coef,
                carbCoefItemStartTime = _state.value.listOfCarbCoefs[index].startTime,
                carbCoefItemEndTime = _state.value.listOfCarbCoefs[index].endTime,
            ))
        }
    }

    private fun onCarbCoefExpandedChange() {
        _state.tryEmit(_state.value.copy(
            isCarbCoefExpanded = !_state.value.isCarbCoefExpanded,
            carbCoefExpandedItemIndex = null
        ))
    }

    private fun onCarbCoefNewValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(carbCoefNewValue = value))
    }

    private fun onCarbCoefNewStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(carbCoefNewStartTime = startTime))
    }

    private fun onCarbCoefNewEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(carbCoefNewEndTime = endTime))
    }

    private fun onCarbCoefItemValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(carbCoefItemValue = value))
    }

    private fun onCarbCoefItemStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(carbCoefItemStartTime = startTime))
    }

    private fun onCarbCoefItemEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(carbCoefItemEndTime = endTime))
    }

    private fun onSaveCarbCoefItem(index: Int) {
        _state.tryEmit(_state.value.copy(
            listOfCarbCoefs = _state.value.listOfCarbCoefs.mapIndexed { i, item ->
                if (i == index) {
                    item.copy(
                        startTime = _state.value.carbCoefItemStartTime,
                        endTime = _state.value.carbCoefItemEndTime,
                        coef = _state.value.carbCoefItemValue
                    )
                } else {
                    item
                }
            },
            carbCoefExpandedItemIndex = null
        ))
    }

    private fun onDeleteCarbCoefItem(index: Int) {
        val newListOfCarbCoef = _state.value.listOfCarbCoefs.toMutableList()
        newListOfCarbCoef.removeAt(index)
        _state.tryEmit(_state.value.copy(
            listOfCarbCoefs = newListOfCarbCoef,
            carbCoefExpandedItemIndex = null
            ))
    }

    private fun onAddCarbCoefItem() {
        val newListOfCarbCoef = _state.value.listOfCarbCoefs.toMutableList()
        newListOfCarbCoef.add(
            CarbCoef(
                id = "",
                startTime = _state.value.carbCoefNewStartTime,
                endTime = _state.value.carbCoefNewEndTime,
                coef = _state.value.carbCoefNewValue
            )
        )
        _state.tryEmit(_state.value.copy(
            listOfCarbCoefs = newListOfCarbCoef,
            carbCoefNewValue = 0.0f,
            carbCoefNewStartTime = "00:00",
            carbCoefNewEndTime = "00:00"
        ))
    }

    // Sensitivity

    private fun onInsSensExpandedItemChange(index: Int) {
        val isEmpty = _state.value.insSensExpandedItemIndex == null
        if(isEmpty){
            _state.tryEmit(_state.value.copy(
                insSensExpandedItemIndex = index,
                insSensItemValue = _state.value.listOfInsSens[index].value,
                insSensItemStartTime = _state.value.listOfInsSens[index].startTime,
                insSensItemEndTime = _state.value.listOfInsSens[index].endTime,
            ))
        } else {
            _state.tryEmit(_state.value.copy(
                insSensExpandedItemIndex = if (index == _state.value.insSensExpandedItemIndex){
                    null
                } else index,
                insSensItemValue = _state.value.listOfInsSens[index].value,
                insSensItemStartTime = _state.value.listOfInsSens[index].startTime,
                insSensItemEndTime = _state.value.listOfInsSens[index].endTime,
            ))
        }
    }

    private fun onInsSensExpandedChange() {
        _state.tryEmit(_state.value.copy(
            isInsSensExpanded = !_state.value.isInsSensExpanded,
            insSensExpandedItemIndex = null
        ))
    }

    private fun onInsSensNewValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(insSensNewValue = value))
    }

    private fun onInsSensNewStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(insSensNewStartTime = startTime))
    }

    private fun onInsSensNewEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(insSensNewEndTime = endTime))
    }

    private fun onInsSensItemValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(insSensItemValue = value))
    }

    private fun onInsSensItemStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(insSensItemStartTime = startTime))
    }

    private fun onInsSensItemEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(insSensItemEndTime = endTime))
    }

    private fun onSaveInsSensItem(index: Int) {
        _state.tryEmit(_state.value.copy(
            listOfInsSens = _state.value.listOfInsSens.mapIndexed { i, item ->
                if (i == index) {
                    item.copy(
                        startTime = _state.value.insSensItemStartTime,
                        endTime = _state.value.insSensItemEndTime,
                        value = _state.value.insSensItemValue
                    )
                } else {
                    item
                }
            },
            insSensExpandedItemIndex = null
        ))
    }

    private fun onDeleteInsSensItem(index: Int) {
        val newListOfInsSens = _state.value.listOfInsSens.toMutableList()
        newListOfInsSens.removeAt(index)
        _state.tryEmit(_state.value.copy(
            listOfInsSens = newListOfInsSens,
            insSensExpandedItemIndex = null
        ))
    }

    private fun onAddInsSensItem() {
        val newListOfInsSens = _state.value.listOfInsSens.toMutableList()
        newListOfInsSens.add(
            InsulinSensitivity(
                id = "",
                startTime = _state.value.insSensNewStartTime,
                endTime = _state.value.insSensNewEndTime,
                value = _state.value.insSensNewValue
            )
        )
        _state.tryEmit(_state.value.copy(
            listOfInsSens = newListOfInsSens,
            insSensNewValue = 0.0f,
            insSensNewStartTime = "00:00",
            insSensNewEndTime = "00:00"
        ))
    }

    // Glucose

    private fun onGlucoseExpandedItemChange(index: Int) {
        val isEmpty = _state.value.glucoseExpandedItemIndex == null
        if(isEmpty){
            _state.tryEmit(_state.value.copy(
                glucoseExpandedItemIndex = index,
                glucoseItemStartValue = _state.value.listOfGlucose[index].startValue,
                glucoseItemEndValue = _state.value.listOfGlucose[index].endValue,
                glucoseItemStartTime = _state.value.listOfGlucose[index].startTime,
                glucoseItemEndTime = _state.value.listOfGlucose[index].endTime,
            ))
        } else {
            _state.tryEmit(_state.value.copy(
                glucoseExpandedItemIndex = if (index == _state.value.insSensExpandedItemIndex){
                    null
                } else index,
                glucoseItemStartValue = _state.value.listOfGlucose[index].startValue,
                glucoseItemEndValue = _state.value.listOfGlucose[index].endValue,
                glucoseItemStartTime = _state.value.listOfGlucose[index].startTime,
                glucoseItemEndTime = _state.value.listOfGlucose[index].endTime,
            ))
        }
    }

    private fun onGlucoseExpandedChange() {
        _state.tryEmit(_state.value.copy(
            isGlucoseExpanded = !_state.value.isGlucoseExpanded,
            glucoseExpandedItemIndex = null
        ))
    }

    private fun onGlucoseNewStartValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(glucoseNewStartValue = value))
    }
    private fun onGlucoseNewEndValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(glucoseNewEndValue = value))
    }

    private fun onGlucoseNewStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(glucoseNewStartTime = startTime))
    }

    private fun onGlucoseNewEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(glucoseNewEndTime = endTime))
    }

    private fun onGlucoseItemStartValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(glucoseItemStartValue = value))
    }
    private fun onGlucoseItemEndValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(glucoseItemEndValue = value))
    }

    private fun onGlucoseItemStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(glucoseItemStartTime = startTime))
    }

    private fun onGlucoseItemEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(glucoseItemEndTime = endTime))
    }

    private fun onSaveGlucoseItem(index: Int) {
        _state.tryEmit(_state.value.copy(
            listOfGlucose = _state.value.listOfGlucose.mapIndexed { i, item ->
                if (i == index) {
                    item.copy(
                        startTime = _state.value.glucoseItemStartTime,
                        endTime = _state.value.glucoseItemEndTime,
                        startValue = _state.value.glucoseItemStartValue,
                        endValue = _state.value.glucoseItemEndValue
                    )
                } else {
                    item
                }
            },
            glucoseExpandedItemIndex = null
        ))
    }

    private fun onDeleteGlucoseItem(index: Int) {
        val newListOfGlucose = _state.value.listOfGlucose.toMutableList()
        newListOfGlucose.removeAt(index)
        _state.tryEmit(_state.value.copy(
            listOfGlucose = newListOfGlucose,
            glucoseExpandedItemIndex = null
        ))
    }

    private fun onAddGlucoseItem() {
        val newListOfGlucose = _state.value.listOfGlucose.toMutableList()
        newListOfGlucose.add(
            TargetGlucose(
                id = "",
                startTime = _state.value.glucoseNewStartTime,
                endTime = _state.value.glucoseNewEndTime,
                startValue = _state.value.glucoseNewStartValue,
                endValue = _state.value.glucoseNewEndValue,
            )
        )
        _state.tryEmit(_state.value.copy(
            listOfGlucose = newListOfGlucose,
            glucoseNewStartValue = 0.0f,
            glucoseNewEndValue = 0.0f,
            glucoseNewStartTime = "00:00",
            glucoseNewEndTime = "00:00"
        ))
    }


    private fun onModeChange() {
        _state.tryEmit(_state.value.copy(isProfileMode = !_state.value.isProfileMode))
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