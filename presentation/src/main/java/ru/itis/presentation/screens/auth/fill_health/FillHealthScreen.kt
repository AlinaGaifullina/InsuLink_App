package ru.itis.presentation.screens.auth.fill_health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.itis.presentation.R
import ru.itis.presentation.components.AuthNumberTextField
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.navigation.graphs.bottom_bar.ProfileNavScreen

@Composable
fun FillHealthScreen(
    navController: NavController,
    viewModel: FillHealthViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            FillHealthSideEffect.NavigateProfile -> navController.navigate(ProfileNavScreen.Profile.route)
            else -> Unit
        }
    }

    FillHealthMainContent(state = state, eventHandler = eventHandler)
}

@Composable
fun FillHealthMainContent(state: FillHealthState, eventHandler: (FillHealthEvent) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.fill_health_info),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = state.weight.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        AuthNumberTextField(
            stringResource(id = R.string.user_height),
            if (state.weight == 0.0f) "" else state.height.toString()
        ) {
            eventHandler.invoke(
                FillHealthEvent.OnHeightChange(
                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                )
            )
        }

        AuthNumberTextField(
            stringResource(id = R.string.user_weight),
            if (state.weight == 0.0f) "" else state.weight.toString()
        ) {
            eventHandler.invoke(
                FillHealthEvent.OnWeightChange(
                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            BaseButton(
                onClick = { eventHandler.invoke(FillHealthEvent.OnDoneButtonClick) },
                text = stringResource(id = R.string.done),
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