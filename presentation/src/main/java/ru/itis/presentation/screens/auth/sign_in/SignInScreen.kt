package ru.itis.presentation.screens.auth.sign_in

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.itis.presentation.navigation.BottomNavigationItem
import ru.itis.presentation.navigation.graphs.AuthNavScreen
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.AuthBottomText
import ru.itis.presentation.components.AuthPasswordField
import ru.itis.presentation.components.AuthTextField
import ru.itis.presentation.components.BaseButton


@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            SignInSideEffect.NavigateProfile -> navController.navigate(BottomNavigationItem.Profile.graph)
            SignInSideEffect.NavigateRegister -> navController.navigate(AuthNavScreen.SignUp.route)
            else -> Unit
        }
    }

    SignInMainContent(state = state, eventHandler = eventHandler)
}

@Composable
fun SignInMainContent(state: SignInState, eventHandler: (SignInEvent) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 20.dp)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = stringResource(id = R.string.sign_in),
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        // fields
        Column {
            AuthTextField(stringResource(id = R.string.email), state.email) {
                eventHandler.invoke(SignInEvent.OnEmailChange(it))
            }

            Spacer(modifier = Modifier.height(20.dp))
            AuthPasswordField(
                stringResource(id = R.string.password), state.password,
                onChange = { eventHandler.invoke(SignInEvent.OnPasswordChange(it)) },
                state.passwordVisible,
                onVisibleChange = { eventHandler.invoke(SignInEvent.OnPasswordVisibilityChange) }
            )
        }

        // Buttons
        Column {
            BaseButton(
                onClick = { eventHandler.invoke(SignInEvent.OnLoginButtonClick) },
                text = stringResource(id = R.string.sign_in),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary
            )
        }

        AuthBottomText(stringResource(id = R.string.account_no), stringResource(id = R.string.sign_up)) {
            eventHandler.invoke(SignInEvent.OnRegisterButtonCLick)
        }
    }
}