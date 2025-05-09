package ru.itis.presentation.screens.auth.sign_in

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.itis.domain.model.Pump
import ru.itis.domain.usecase.SaveLocalPumpUseCase
import ru.itis.domain.usecase.datastore.SavePumpIdInDataStoreUseCase
import ru.itis.domain.usecase.datastore.SaveUserIdInDataStoreUseCase


data class SignInState(
    val password: String = "",
    val email: String = "",
    val passwordVisible: Boolean = false,
    val showLoadingProgressBar: Boolean = false,
    val showErrorDialog: Boolean = false,
    val showLoginError: Boolean = false,
    val loginError: String = "",
    val errors: List<String> = listOf()
)

sealed interface SignInSideEffect {
    object NavigateProfile : SignInSideEffect
    object NavigateRegister : SignInSideEffect
}

sealed interface SignInEvent {
    object OnLoginButtonClick : SignInEvent
    object OnRegisterButtonCLick : SignInEvent
    object OnPasswordVisibilityChange : SignInEvent
    data class OnEmailChange(val value: String) : SignInEvent
    data class OnPasswordChange(val value: String) : SignInEvent
}

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val saveUserIdInDataStoreUseCase: SaveUserIdInDataStoreUseCase,
    private val savePumpIdInDataStoreUseCase: SavePumpIdInDataStoreUseCase,
    private val saveLocalPumpUseCase: SaveLocalPumpUseCase,
) : ViewModel(){
    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state

    private val _action = MutableSharedFlow<SignInSideEffect?>()
    val action: SharedFlow<SignInSideEffect?>
        get() = _action.asSharedFlow()

    fun event(event: SignInEvent) {
        when (event) {
            SignInEvent.OnPasswordVisibilityChange -> onPasswordVisibilityChange()
            is SignInEvent.OnPasswordChange -> onPasswordChange(event.value)
            is SignInEvent.OnEmailChange -> onEmailChange(event.value)
            SignInEvent.OnLoginButtonClick -> onLoginButtonClick()
            SignInEvent.OnRegisterButtonCLick -> onRegisterButtonCLick()
        }
    }

    private fun onPasswordChange(password: String) {
        _state.tryEmit(_state.value.copy(password = password, showLoginError = false))
    }

    private fun onEmailChange(email: String) {
        _state.tryEmit(_state.value.copy(email = email))
    }

    private fun onPasswordVisibilityChange() {
        _state.tryEmit(_state.value.copy(passwordVisible = !_state.value.passwordVisible))
    }

    private fun onRegisterButtonCLick() {
        viewModelScope.launch {
            saveUserIdInDataStoreUseCase("1")
            _action.emit(SignInSideEffect.NavigateRegister
            )
        }
    }

    private fun onLoginButtonClick() {
        viewModelScope.launch {
            saveUserIdInDataStoreUseCase("1")
            savePumpIdInDataStoreUseCase("1")
            saveLocalPumpUseCase(
                Pump(
                    id = "1",
                    deviceId = "123",
                    carbMeasurementUnit = "1",
                    glucoseMeasurementUnit = "1",
                    insulinActiveTime = 2.0f,
                    userId = "1"
                )
            )
            _action.emit(SignInSideEffect.NavigateProfile)
        }
    }

//
//    private fun onLoginButtonClick() {
//        var loginError = ""
//        viewModelScope.launch {
//            val errors = mutableListOf<String>()
//            _state.emit(_state.value.copy(showLoadingProgressBar = true))
//            val result = if (validateFields(errors)) with(state.value) {
//                login(phone, password)
//            }
//            else LoginResult.FailLogin()
//            _state.emit(_state.value.copy(showLoadingProgressBar = false))
//
//            when (result) {
//                is LoginResult.SuccessLogin -> {
//
//                    _action.emit(LoginSideEffect.NavigateProfile)
//                }
//
//                is LoginResult.FailLogin -> {
//                    result.errorMessage?.let { loginError = it }
//                    _state.emit(_state.value.copy(showLoginError = true, loginError = loginError))
//                }
//            }
//        }
//    }

    private fun validateFields(errors: MutableList<String>): Boolean {

        var passValidate = true
        val password = state.value.password
        if (!password.matches(Regex(".*[a-z].*"))) {
            errors.add("Пароль должен содержать хотя бы одну маленькую букву.")
            passValidate = false
        }

        if (!password.matches(Regex(".*[A-Z].*"))) {
            errors.add("Пароль должен содержать хотя бы одну большую букву.")
            passValidate = false
        }

        if (!password.matches(Regex(".*\\d.*"))) {
            passValidate = false
            errors.add("Пароль должен содержать хотя бы одну цифру.")
        }

        if (password.length < 8) {
            passValidate = false
            errors.add("Пароль должен содержать минимум 8 символов.")
        }

        return passValidate
    }
}