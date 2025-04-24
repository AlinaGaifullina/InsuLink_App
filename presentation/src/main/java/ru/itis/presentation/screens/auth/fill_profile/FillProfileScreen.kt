package ru.itis.presentation.screens.auth.fill_profile

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
import ru.itis.presentation.navigation.graphs.AuthNavScreen
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.AuthTextField
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.components.BirthDateTextField
import ru.itis.presentation.components.SwitchButton


@Composable
fun FillProfileScreen(
    navController: NavController,
    viewModel: FillProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            FillProfileSideEffect.NavigateFillHealth -> navController.navigate(AuthNavScreen.FillHealth.route)
            else -> Unit
        }
    }

    FillProfileMainContent(state = state, eventHandler = eventHandler)
}

@Composable
fun FillProfileMainContent(state: FillProfileState, eventHandler: (FillProfileEvent) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.fill_personal_info),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(50.dp))
            BirthDateTextField(stringResource(id = R.string.user_birth_date), state.birthDate) {
                eventHandler.invoke(FillProfileEvent.OnBirthDateChange(it))
            }
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(stringResource(id = R.string.user_name), state.name) {
                eventHandler.invoke(FillProfileEvent.OnNameChange(it))
            }
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(stringResource(id = R.string.user_surname), state.surname) {
                eventHandler.invoke(FillProfileEvent.OnSurnameChange(it))
            }
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(stringResource(id = R.string.user_patronymic), state.patronymic) {
                eventHandler.invoke(FillProfileEvent.OnPatronymicChange(it))
            }
            Spacer(modifier = Modifier.height(20.dp))
            SwitchButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                selectedColor = MaterialTheme.colorScheme.surfaceTint,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(0.3f),
                textSelectedColor = MaterialTheme.colorScheme.onSecondary,
                textBackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(0.8f),
                height = 56.dp,
                titleFirst = stringResource(R.string.gender_male),
                titleSecond = stringResource(R.string.gender_female)
            ) { isMale ->
                if(isMale != state.isMale) eventHandler.invoke(FillProfileEvent.OnGenderChange)
            }
        }

        // Buttons
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            BaseButton(
                onClick = { eventHandler.invoke(FillProfileEvent.OnNextButtonClick) },
                text = stringResource(id = R.string.next),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}