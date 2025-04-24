package ru.itis.presentation.screens.auth.greeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.navigation.graphs.AuthNavScreen
import ru.itis.presentation.navigation.graphs.bottom_bar.ProfileNavScreen

@Composable
fun GreetingScreen(
    navController: NavController,
    viewModel: GreetingViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            GreetingSideEffect.NavigateProfile -> navController.navigate(ProfileNavScreen.Profile.route)
            GreetingSideEffect.NavigateFillProfile -> navController.navigate(AuthNavScreen.FillProfile.route)
            else -> Unit
        }
    }

    val view = LocalView.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(top = 140.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.greeting_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = stringResource(R.string.greeting_text),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                BaseButton(
                    onClick = { eventHandler.invoke(GreetingEvent.OnFillProfileButtonClick) },
                    text = stringResource(R.string.fill_profile),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)
                    ,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary
                )
                BaseButton(
                    onClick = { eventHandler.invoke(GreetingEvent.OnSkipButtonCLick) },
                    text = stringResource(R.string.skip),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)
                    ,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}