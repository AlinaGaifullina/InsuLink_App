package ru.itis.presentation.screens.profile.profile

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.domain.bluetooth.BluetoothDiscoveryManager
import ru.itis.domain.bluetooth.BluetoothManager
import ru.itis.domain.bluetooth.PumpBluetoothController
import ru.itis.domain.bluetooth.PumpEvent
import ru.itis.domain.model.CarbCoef
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.domain.model.TargetGlucose
import ru.itis.domain.usecase.datastore.DeleteUserIdFromDataStoreUseCase
import ru.itis.domain.usecase.datastore.GetIsInsulinInjectionFromDataStoreUseCase
import ru.itis.domain.usecase.datastore.GetUserIdFromDataStoreUseCase
import ru.itis.domain.usecase.pump_settings.carb_coef.DeleteLocalCarbCoefUseCase
import ru.itis.domain.usecase.pump_settings.carb_coef.GetLocalAllCarbCoefsUseCase
import ru.itis.domain.usecase.pump_settings.carb_coef.SaveLocalCarbCoefUseCase
import ru.itis.domain.usecase.pump_settings.carb_coef.UpdateLocalCarbCoefUseCase
import ru.itis.domain.usecase.pump_settings.insulin_sensitivity.DeleteLocalInsulinSensitivityUseCase
import ru.itis.domain.usecase.pump_settings.insulin_sensitivity.GetLocalAllInsulinSensitivityUseCase
import ru.itis.domain.usecase.pump_settings.insulin_sensitivity.SaveLocalInsulinSensitivityUseCase
import ru.itis.domain.usecase.pump_settings.insulin_sensitivity.UpdateLocalInsulinSensitivityUseCase
import ru.itis.domain.usecase.pump_settings.target_glucose.DeleteLocalTargetGlucoseUseCase
import ru.itis.domain.usecase.pump_settings.target_glucose.GetLocalAllTargetGlucoseUseCase
import ru.itis.domain.usecase.pump_settings.target_glucose.SaveLocalTargetGlucoseUseCase
import ru.itis.domain.usecase.pump_settings.target_glucose.UpdateLocalTargetGlucoseUseCase
import ru.itis.presentation.utils.IdGenerator
import ru.itis.presentation.utils.getActiveTimeInHours
import java.io.IOException
import javax.inject.Inject


data class BluetoothDeviceUI(
    val name: String,
    val address: String,
    val isConnecting: Boolean = false
)

sealed class ConnectionProgress {
    object Idle : ConnectionProgress()
    object Searching : ConnectionProgress()
    object Connecting : ConnectionProgress()
    object Connected : ConnectionProgress()
    data class Error(val message: String) : ConnectionProgress()
}

data class ProfileState(
    val name: String = "Иван",
    val surname: String = "Иванов",
    val patronymic: String = "Иванович",
    val birthDate: String = "05.04.2003",
    val isMale: Boolean? = true,
    val height: Float = 190.0f,
    val weight: Float = 90.0f,
    val insulin: String = "Хумалог",
    val isProfileMode: Boolean = true,
    val isBreadUnits: Boolean = true,
    val isMmolLiter: Boolean = true,
    val activeInsulinTime: Float = 5.0f,

    // Carb
    val listOfCarbCoefs: List<CarbCoef> = listOf(),
    val isCarbCoefExpanded: Boolean = false,
    val carbCoefExpandedItemIndex: Int? = null,
    val carbCoefNewStartTime: String = "00:00",
    val carbCoefNewEndTime: String = "03:00",
    val carbCoefNewValue: Float = 0.0f,
    val carbCoefItemStartTime: String = "00:00",
    val carbCoefItemEndTime: String = "03:00",
    val carbCoefItemValue: Float = 0.0f,

    // Sensitivity
    val listOfInsSens: List<InsulinSensitivity> = listOf(),
    val isInsSensExpanded: Boolean = false,
    val insSensExpandedItemIndex: Int? = null,
    val insSensNewStartTime: String = "00:00",
    val insSensNewEndTime: String = "03:00",
    val insSensNewValue: Float = 0.0f,
    val insSensItemStartTime: String = "00:00",
    val insSensItemEndTime: String = "03:00",
    val insSensItemValue: Float = 0.0f,

    // Glucose
    val listOfGlucose: List<TargetGlucose> = listOf(),
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

    val isPumpSettingsLoading: Boolean = false,
    val pumpSettingsLoadingError: String? = null,

    // Bluetooth
    val isPumpConnected: Boolean = false,
    val pumpName: String = "Инсулиновая помпа",
    val isSearchingDevices: Boolean = false,
    val foundDevices: List<BluetoothDeviceUI> = emptyList(),
    val allFoundDevices: List<BluetoothDevice> = emptyList(),
    val connectionProgress: ConnectionProgress = ConnectionProgress.Idle,
    val bluetoothEnabled: Boolean = false,
    val bluetoothData: String = "",
    val isReceivingData: Boolean = false
)

sealed interface ProfileEvent {
    object OnLogOutButtonClick : ProfileEvent
    object OnEditProfileButtonClick : ProfileEvent
    object OnModeChange : ProfileEvent
    object OnCarbUnitChange : ProfileEvent
    object OnGlucoseUnitChange : ProfileEvent
    object OnDelegateButtonClick: ProfileEvent
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

    // Bluetooth
    object OnConnectButtonClick : ProfileEvent
    object OnDisconnectButtonClick : ProfileEvent
    data class OnDeviceSelected(val value: BluetoothDeviceUI) : ProfileEvent
    object OnSearchDevicesClick : ProfileEvent
    object OnCancelSearchClick : ProfileEvent
    data class OnEnableBluetooth(val value: Activity) : ProfileEvent
}

sealed interface ProfileSideEffect {
    object NavigateEditProfileScreen : ProfileSideEffect
    object NavigateBolusInjection : ProfileSideEffect
    object NavigateAccessDelegation : ProfileSideEffect
    object NavigateSignInScreen : ProfileSideEffect
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bluetoothManager: BluetoothManager,
    private val bluetoothDiscoveryManager: BluetoothDiscoveryManager,
    private val pumpBluetoothController: PumpBluetoothController,

    private val getUserIdFromDataStoreUseCase: GetUserIdFromDataStoreUseCase,
    private val deleteUserIdFromDataStoreUseCase: DeleteUserIdFromDataStoreUseCase,
    private val getIsInsulinInjectionFromDataStoreUseCase: GetIsInsulinInjectionFromDataStoreUseCase,

    private val getLocalAllCarbCoefsUseCase: GetLocalAllCarbCoefsUseCase,
    private val getLocalAllInsulinSensitivityUseCase: GetLocalAllInsulinSensitivityUseCase,
    private val getLocalAllTargetGlucoseUseCase: GetLocalAllTargetGlucoseUseCase,

    private val saveLocalCarbCoefUseCase: SaveLocalCarbCoefUseCase,
    private val saveLocalInsulinSensitivityUseCase: SaveLocalInsulinSensitivityUseCase,
    private val saveLocalTargetGlucoseUseCase: SaveLocalTargetGlucoseUseCase,

    private val updateLocalCarbCoefUseCase: UpdateLocalCarbCoefUseCase,
    private val updateLocalInsulinSensitivityUseCase: UpdateLocalInsulinSensitivityUseCase,
    private val updateLocalTargetGlucoseUseCase: UpdateLocalTargetGlucoseUseCase,

    private val deleteLocalCarbCoefUseCase: DeleteLocalCarbCoefUseCase,
    private val deleteLocalInsulinSensitivityUseCase: DeleteLocalInsulinSensitivityUseCase,
    private val deleteLocalTargetGlucoseUseCase: DeleteLocalTargetGlucoseUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _action = MutableSharedFlow<ProfileSideEffect?>()
    val action: SharedFlow<ProfileSideEffect?>
        get() = _action.asSharedFlow()

    private var userId: String = ""

    init {
        loadUserData()
        checkBluetoothState()
        setupBluetoothListener()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                // 1. Получаем ID через UseCase
                userId = getUserIdFromDataStoreUseCase()
                    ?: throw IllegalStateException("User not authenticated")

                val isInsulinInjection = getIsInsulinInjectionFromDataStoreUseCase()
                if(isInsulinInjection){
                    _action.emit(ProfileSideEffect.NavigateBolusInjection)
                }

                // 2. Загружаем данные пользователя
//                val userData = getUserDataUseCase(userId)
//
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        userData = userData
//                    )
//                }

            } catch (e: Exception) {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        error = e.message
//                    )
//                }
            }
        }
    }
    private fun checkBluetoothState() {
        _state.update { it.copy(bluetoothEnabled = bluetoothManager.isBluetoothEnabled()) }
    }


    private fun loadPumpSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isPumpSettingsLoading = true, pumpSettingsLoadingError = null) }
            try {
                val carbCoefsList = getLocalAllCarbCoefsUseCase()
                val insulinSensitivityList = getLocalAllInsulinSensitivityUseCase()
                val targetGlucoseList = getLocalAllTargetGlucoseUseCase()

                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            isPumpSettingsLoading = false,
                            listOfCarbCoefs = carbCoefsList,
                            listOfInsSens = insulinSensitivityList,
                            listOfGlucose = targetGlucoseList
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isPumpSettingsLoading = false,
                        pumpSettingsLoadingError = "Failed to load data"
                    )
                }
            }
        }
    }

    private fun setupBluetoothListener() {
        viewModelScope.launch {
            pumpBluetoothController.startListening { event ->
                _state.update { state ->
                    state.copy(
                        bluetoothData = when (event) {
                            is PumpEvent.InsulinDelivery ->
                                "Доставка инсулина: ${event.units} ЕД в ${event.timestamp}"
                            is PumpEvent.EmergencyStop ->
                                "Экстренная остановка в ${event.timestamp}"
                            is PumpEvent.ErrorEvent ->
                                "Ошибка: ${event.errorCode}"
                            is PumpEvent.ReservoirChange ->
                                if (event.isCompleted) "Резервуар заменён"
                                else "Начата замена резервуара"
                        },
                        isReceivingData = true
                    )
                }

                // Автоматическое скрытие через 5 секунд
                viewModelScope.launch {
                    delay(5000)
                    _state.update { it.copy(isReceivingData = false) }
                }
            }
        }
    }

    fun connectToPump(device: BluetoothDevice) {
        viewModelScope.launch {
            //_state.update { it.copy(isLoading = true) }
            pumpBluetoothController.connectToPump(device)
                .onSuccess {
                    _state.update { it.copy(isPumpConnected = true) }
                }
                .onFailure {
                    //_state.update { it.copy(error = "Ошибка подключения") }
                }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    fun event(profileEvent: ProfileEvent) {
        ViewModelProvider.NewInstanceFactory
        when (profileEvent) {
            ProfileEvent.OnLogOutButtonClick -> onLogOutButtonClick()
            ProfileEvent.OnEditProfileButtonClick -> onEditProfileButtonClick()
            ProfileEvent.OnDelegateButtonClick -> onDelegateButtonClick()
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

            // Bluetooth
            ProfileEvent.OnConnectButtonClick -> onConnectButtonClick()
            ProfileEvent.OnDisconnectButtonClick -> onDisconnectButtonClick()
            is ProfileEvent.OnDeviceSelected -> onDeviceSelected(profileEvent.value)
            ProfileEvent.OnSearchDevicesClick -> onSearchDevicesClick()
            ProfileEvent.OnCancelSearchClick -> onCancelSearchClick()
            is ProfileEvent.OnEnableBluetooth -> onEnableBluetooth(profileEvent.value)
        }
    }

    private fun onLogOutButtonClick() {
        viewModelScope.launch {
            deleteUserIdFromDataStoreUseCase()
            _action.emit(ProfileSideEffect.NavigateSignInScreen)
        }
    }

    private fun onInsulinChange(insulin: String) {
        _state.tryEmit(_state.value.copy(
            insulin = insulin,
            activeInsulinTime = getActiveTimeInHours(insulin) ?: 0.0f
        ))
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
        val updatedCarbCoef = _state.value.listOfCarbCoefs[index]
        viewModelScope.launch {
            try {
                updateLocalCarbCoefUseCase(
                    updatedCarbCoef.copy(
                        startTime = _state.value.carbCoefItemStartTime,
                        endTime = _state.value.carbCoefItemEndTime,
                        coef = _state.value.carbCoefItemValue
                    )
                )
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
            } catch (e: Exception) {

            }
        }
    }

    private fun onDeleteCarbCoefItem(index: Int) {
        val newListOfCarbCoef = _state.value.listOfCarbCoefs.toMutableList()
        val removedCarbCoef = newListOfCarbCoef[index]
        newListOfCarbCoef.removeAt(index)

        viewModelScope.launch {
            try {
                deleteLocalCarbCoefUseCase(removedCarbCoef.id)
                _state.tryEmit(_state.value.copy(
                    listOfCarbCoefs = newListOfCarbCoef,
                    carbCoefExpandedItemIndex = null
                ))
            } catch (e: Exception) {

            }
        }
    }

    private fun onAddCarbCoefItem() {
        val newListOfCarbCoef = _state.value.listOfCarbCoefs.toMutableList()
        val newCarbCoef = CarbCoef(
            id = IdGenerator.newId(),
            userId = userId,
            startTime = _state.value.carbCoefNewStartTime,
            endTime = _state.value.carbCoefNewEndTime,
            coef = _state.value.carbCoefNewValue
        )
        newListOfCarbCoef.add(newCarbCoef)

        viewModelScope.launch {
            try {
                saveLocalCarbCoefUseCase(newCarbCoef)
                _state.tryEmit(_state.value.copy(
                    listOfCarbCoefs = newListOfCarbCoef,
                    carbCoefNewValue = 0.0f,
                    carbCoefNewStartTime = "00:00",
                    carbCoefNewEndTime = "00:00"
                ))
            } catch (e: Exception) {

            }
        }
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
        val updatedInsSens = _state.value.listOfInsSens[index]
        viewModelScope.launch {
            try {
                updateLocalInsulinSensitivityUseCase(
                    updatedInsSens.copy(
                        startTime = _state.value.insSensItemStartTime,
                        endTime = _state.value.insSensItemEndTime,
                        value = _state.value.insSensItemValue
                    )
                )
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
            } catch (e: Exception) {

            }
        }
    }

    private fun onDeleteInsSensItem(index: Int) {
        val newListOfInsSens = _state.value.listOfInsSens.toMutableList()
        val removedInsSens = newListOfInsSens[index]
        newListOfInsSens.removeAt(index)
        viewModelScope.launch {
            try {
                deleteLocalInsulinSensitivityUseCase(removedInsSens.id)
                _state.tryEmit(_state.value.copy(
                    listOfInsSens = newListOfInsSens,
                    insSensExpandedItemIndex = null
                ))
            } catch (e: Exception) {

            }
        }
    }

    private fun onAddInsSensItem() {
        val newListOfInsSens = _state.value.listOfInsSens.toMutableList()
        val newInsSens = InsulinSensitivity(
            id = IdGenerator.newId(),
            userId = userId,
            startTime = _state.value.insSensNewStartTime,
            endTime = _state.value.insSensNewEndTime,
            value = _state.value.insSensNewValue
        )
        newListOfInsSens.add(newInsSens)

        viewModelScope.launch {
            try {
                saveLocalInsulinSensitivityUseCase(newInsSens)
                _state.tryEmit(_state.value.copy(
                    listOfInsSens = newListOfInsSens,
                    insSensNewValue = 0.0f,
                    insSensNewStartTime = "00:00",
                    insSensNewEndTime = "00:00"
                ))
            } catch (e: Exception) {

            }
        }
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
        val updatedGlucose = _state.value.listOfGlucose[index]
        viewModelScope.launch {
            try {
                updateLocalTargetGlucoseUseCase(
                    updatedGlucose.copy(
                        startTime = _state.value.glucoseItemStartTime,
                        endTime = _state.value.glucoseItemEndTime,
                        startValue = _state.value.glucoseItemStartValue,
                        endValue = _state.value.glucoseItemEndValue
                    )
                )
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
            } catch (e: Exception) {

            }
        }
    }

    private fun onDeleteGlucoseItem(index: Int) {
        val newListOfGlucose = _state.value.listOfGlucose.toMutableList()
        val removedGlucose = newListOfGlucose[index]
        newListOfGlucose.removeAt(index)
        viewModelScope.launch {
            try {
                deleteLocalTargetGlucoseUseCase(removedGlucose.id)
                _state.tryEmit(_state.value.copy(
                    listOfGlucose = newListOfGlucose,
                    glucoseExpandedItemIndex = null
                ))
            } catch (e: Exception) {

            }
        }
    }

    private fun onAddGlucoseItem() {
        val newListOfGlucose = _state.value.listOfGlucose.toMutableList()
        val newGlucose = TargetGlucose(
            id = IdGenerator.newId(),
            userId = userId,
            startTime = _state.value.glucoseNewStartTime,
            endTime = _state.value.glucoseNewEndTime,
            startValue = _state.value.glucoseNewStartValue,
            endValue = _state.value.glucoseNewEndValue,
        )
        newListOfGlucose.add(newGlucose)

        viewModelScope.launch {
            try {
                saveLocalTargetGlucoseUseCase(newGlucose)
                _state.tryEmit(_state.value.copy(
                    listOfGlucose = newListOfGlucose,
                    glucoseNewStartValue = 0.0f,
                    glucoseNewEndValue = 0.0f,
                    glucoseNewStartTime = "00:00",
                    glucoseNewEndTime = "00:00"
                ))
            } catch (e: Exception) {

            }
        }
    }

    private fun onModeChange() {
        _state.tryEmit(_state.value.copy(isProfileMode = !_state.value.isProfileMode))
        if(!_state.value.isProfileMode){
            viewModelScope.launch {
                loadPumpSettings()
            }
        }
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

    private fun onDelegateButtonClick() {
        viewModelScope.launch { _action.emit(ProfileSideEffect.NavigateAccessDelegation) }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun onEnableBluetooth(activity: Activity) {
        bluetoothManager.enableBluetooth(activity)
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    private fun onConnectButtonClick() {
        if (!state.value.bluetoothEnabled) {
            _state.update { it.copy(connectionProgress = ConnectionProgress.Error("Включите Bluetooth")) }
            return
        }

        _state.update { it.copy(isSearchingDevices = true) }
        onSearchDevicesClick()
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    private fun onSearchDevicesClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSearchingDevices = true,
                    foundDevices = emptyList(),
                    connectionProgress = ConnectionProgress.Searching
                )
            }

            try {
                val pairedDevices = bluetoothManager.getPairedDevices()
                val discoveredDevices = discoverDevices()

                val allDevices = (pairedDevices + discoveredDevices)
                    .distinctBy { it.address }
                    .map { BluetoothDeviceUI(it.name ?: "Unknown", it.address) }

                _state.update { state ->
                    state.copy(
                        isSearchingDevices = false,
                        foundDevices = allDevices,
                        connectionProgress = ConnectionProgress.Idle
                    )
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        isSearchingDevices = false,
                        connectionProgress = ConnectionProgress.Error(
                            e.message ?: "Ошибка поиска устройств"
                        )
                    )
                }
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private suspend fun discoverDevices(): List<BluetoothDevice> {
        return bluetoothDiscoveryManager.discoverDevices()
    }
    fun logDeviceInfo(device: BluetoothDevice) {
        Log.d("Bluetooth", """
        Device: ${device.name} [${device.address}]
        Type: ${when(device.type) {
            BluetoothDevice.DEVICE_TYPE_CLASSIC -> "CLASSIC"
            BluetoothDevice.DEVICE_TYPE_LE -> "BLE"
            BluetoothDevice.DEVICE_TYPE_DUAL -> "DUAL"
            else -> "UNKNOWN"
        }}
        BondState: ${when(device.bondState) {
            BluetoothDevice.BOND_BONDED -> "BONDED"
            BluetoothDevice.BOND_BONDING -> "BONDING"
            else -> "NONE"
        }}
        UUIDS: ${device.uuids?.joinToString() ?: "None"}
    """.trimIndent())
    }


    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    private fun onDeviceSelected(device: BluetoothDeviceUI) {
        viewModelScope.launch {
            try {
                val bluetoothDevice = bluetoothManager.getPairedDevices()
                    .firstOrNull { it.address == device.address }
                    ?: throw IOException("Устройство не спарено. Сначала сопрягите его в настройках Bluetooth")

                // Пропускаем проверку на тип устройства
                val result = bluetoothManager.connectToDevice(bluetoothDevice)
                logDeviceInfo(bluetoothDevice)
                if (result.isSuccess) {
                    _state.update { state ->
                        state.copy(
                            isPumpConnected = true,
                            pumpName = "[TEST] ${bluetoothDevice.name ?: device.name}",
                            connectionProgress = ConnectionProgress.Connected
                        )
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
//    private fun onDeviceSelected(device: BluetoothDeviceUI) {
//        _state.update { state ->
//            state.copy(
//                foundDevices = state.foundDevices.map {
//                    if (it.address == device.address) it.copy(isConnecting = true)
//                    else it
//                },
//                connectionProgress = ConnectionProgress.Connecting
//            )
//        }
//
//        viewModelScope.launch {
//            try {
//                // 1. Ищем среди всех найденных устройств
//                val bluetoothDevice = state.value.allFoundDevices
//                    .firstOrNull { it.address == device.address }
//                // 2. Если не найдено, проверяем спаренные устройства
//                    ?: bluetoothManager.getPairedDevices()
//                        .firstOrNull { it.address == device.address }
//                    // 3. Если все равно не найдено - ошибка
//                    ?: throw IOException("""
//                    Устройство ${device.name} недоступно.
//                    Убедитесь, что:
//                    - Помпа включена
//                    - Bluetooth активирован
//                    - Устройство в зоне действия
//                """.trimIndent())
//
//                // 4. Подключаемся
//                val result = bluetoothManager.connectToDevice(bluetoothDevice)
//
//                if (result.isSuccess) {
//                    _state.update { state ->
//                        state.copy(
//                            isPumpConnected = true,
//                            pumpName = bluetoothDevice.name ?: device.name,
//                            connectionProgress = ConnectionProgress.Connected,
//                            foundDevices = emptyList(),
//                            allFoundDevices = emptyList()
//                        )
//                    }
//                } else {
//                    throw result.exceptionOrNull() ?: IOException("Не удалось установить соединение")
//                }
//            } catch (e: Exception) {
//                _state.update { state ->
//                    state.copy(
//                        connectionProgress = ConnectionProgress.Error(
//                            e.message ?: "Ошибка подключения"
//                        ),
//                        foundDevices = state.foundDevices.map {
//                            if (it.address == device.address) it.copy(isConnecting = false)
//                            else it
//                        }
//                    )
//                }
//            }
//        }
//    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun onCancelSearchClick() {
        bluetoothManager.cancelDiscovery()
        _state.update {
            it.copy(
                isSearchingDevices = false,
                connectionProgress = ConnectionProgress.Idle
            )
        }
    }

    private fun onDisconnectButtonClick() {
        bluetoothManager.disconnect()
        _state.update {
            it.copy(
                isPumpConnected = false,
                pumpName = "Инсулиновая помпа",
                connectionProgress = ConnectionProgress.Idle
            )
        }
    }
}