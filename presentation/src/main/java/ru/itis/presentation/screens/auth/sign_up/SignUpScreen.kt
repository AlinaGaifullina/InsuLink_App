package ru.itis.presentation.screens.auth.sign_up

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.navigation.graphs.AuthNavScreen
import ru.itis.presentation.components.AuthBottomText
import ru.itis.presentation.components.AuthPasswordField
import ru.itis.presentation.components.AuthTextField
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import ru.itis.presentation.components.BaseButton


@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            SignUpSideEffect.NavigateGreeting -> navController.navigate(AuthNavScreen.Greeting.route)
            SignUpSideEffect.NavigateLogin -> navController.navigateUp()
            else -> Unit
        }
    }

    SignUpMainContent(state = state, eventHandler = eventHandler)
}

@Composable
private fun SignUpMainContent(state: SignUpState, eventHandler: (SignUpEvent) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        // fields
        Column {
            AuthTextField(
                stringResource(id = R.string.user_name),
                state.name
            ) { eventHandler.invoke(SignUpEvent.OnNameChange(it)) }

            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                stringResource(id = R.string.email),
                state.email
            ) { eventHandler.invoke(SignUpEvent.OnEmailChange(it)) }

            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(
                stringResource(id = R.string.password),
                state.password,
                { eventHandler.invoke(SignUpEvent.OnPasswordChange(it)) },
                state.passwordVisible,
                { eventHandler.invoke(SignUpEvent.OnPasswordVisibilityChange) })

            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(
                stringResource(id = R.string.repeat_password),
                state.confirmPassword,
                { eventHandler.invoke(SignUpEvent.OnConfirmPasswordChange(it)) },
                state.passwordVisible,
                { eventHandler.invoke(SignUpEvent.OnPasswordVisibilityChange) })
        }

        // Buttons
        Column {
            BaseButton(
                onClick = { eventHandler.invoke(SignUpEvent.OnRegisterButtonClick) },
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary
            )
        }

        AuthBottomText(
            stringResource(id = R.string.account_yes),
            stringResource(id = R.string.sign_in)
        ) { eventHandler.invoke(SignUpEvent.OnLoginButtonCLick) }
    }
}