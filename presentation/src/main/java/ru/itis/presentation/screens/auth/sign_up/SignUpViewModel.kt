package ru.itis.presentation.screens.auth.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SignUpState(
    val name: String = "",
    val password: String = "",
    val email: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val showLoadingProgressBar: Boolean = false,
    val showErrors: Boolean = false,
    val isMainAccount: Boolean = true,
    val loginError: String = "",
    val errors: List<String> = listOf()
)

sealed interface SignUpSideEffect {
    object NavigateGreeting : SignUpSideEffect
    object NavigateLogin : SignUpSideEffect
}

sealed interface SignUpEvent {
    object OnRegisterButtonClick : SignUpEvent
    object OnLoginButtonCLick : SignUpEvent
    object OnPasswordVisibilityChange : SignUpEvent
    object OnIsMainAccountChange : SignUpEvent
    data class OnEmailChange(val value: String) : SignUpEvent
    data class OnNameChange(val value: String) : SignUpEvent
    data class OnPasswordChange(val value: String) : SignUpEvent
    data class OnConfirmPasswordChange(val value: String) : SignUpEvent
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    //private val isUsernameExist: CheckUsernameUseCase,
) : ViewModel(){
    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(
        SignUpState()
    )
    val state: StateFlow<SignUpState> = _state

    private val _action = MutableSharedFlow<SignUpSideEffect?>()
    val action: SharedFlow<SignUpSideEffect?>
        get() = _action.asSharedFlow()

    fun event(event: SignUpEvent) {
        when (event) {
            SignUpEvent.OnRegisterButtonClick -> onRegisterButtonClick()
            SignUpEvent.OnLoginButtonCLick -> onLoginButtonClick()
            SignUpEvent.OnPasswordVisibilityChange -> onPasswordVisibilityChange()
            SignUpEvent.OnIsMainAccountChange -> onIsMainAccountChange()
            is SignUpEvent.OnNameChange -> onNameChange(event.value)
            is SignUpEvent.OnEmailChange -> onEmailChange(event.value)
            is SignUpEvent.OnPasswordChange -> onPasswordChange(event.value)
            is SignUpEvent.OnConfirmPasswordChange -> onConfirmPasswordChange(event.value)
        }
    }

    private fun onNameChange(name: String) {
        _state.tryEmit(_state.value.copy(name = name))
    }

    private fun onEmailChange(email: String) {
        _state.tryEmit(_state.value.copy(email = email))
    }

    private fun onPasswordChange(password: String) {
        _state.tryEmit(_state.value.copy(password = password))
    }

    private fun onConfirmPasswordChange(password: String) {
        _state.tryEmit(_state.value.copy(confirmPassword = password))
    }

    private fun onPasswordVisibilityChange() {
        _state.tryEmit(_state.value.copy(passwordVisible = !_state.value.passwordVisible))
    }

    private fun onIsMainAccountChange() {
        _state.tryEmit(_state.value.copy(isMainAccount = !_state.value.isMainAccount))
    }


    private fun onLoginButtonClick() {
        viewModelScope.launch {
            _action.emit(SignUpSideEffect.NavigateLogin)
        }
    }

    private fun onRegisterButtonClick() {
        viewModelScope.launch {
            _action.emit(SignUpSideEffect.NavigateGreeting)
        }

//        viewModelScope.launch {
//            val errors = mutableListOf<String>()
//            _state.emit(_state.value.copy(showLoadingProgressBar = true))
//            val result =
//                if (validateFields(errors)) with(state.value) {
//                    register(firstName, lastName, phone, password)
//                }
//                else RegisterResult.FailRegister()
//            _state.emit(_state.value.copy(
//                showLoadingProgressBar = false,
//                errors = errors )
//            )
//
//            when (result) {
//                is RegisterResult.SuccessRegister -> {
//                    addCartUseCase(_state.value.phone, "г. Казань, ул. Черноморская, д. 5, кв. 42")
//                    _action.emit(RegisterSideEffect.NavigateProfile)
//                }
//
//                is RegisterResult.FailRegister -> {
//                    result.errorMessage?.let { errors.add(it) }
//                    _state.emit(_state.value.copy(showErrors = true, errors = errors))
//                }
//            }
//        }
    }


    private fun validateFields(errors: MutableList<String>): Boolean {
//        if (state.value.phone.length < 3) {
//            errors.add("Логин должен состоять не менее чем из 3 символов.")
//            return false
//        }

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

        if (password != state.value.confirmPassword) {
            passValidate = false
            errors.add("Пароли не совпадают.")
        }

        return passValidate
    }
}