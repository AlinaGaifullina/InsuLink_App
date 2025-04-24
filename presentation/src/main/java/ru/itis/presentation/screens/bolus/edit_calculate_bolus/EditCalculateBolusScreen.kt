package ru.itis.presentation.screens.bolus.edit_calculate_bolus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.navigation.graphs.BolusNavScreen
import ru.itis.presentation.navigation.graphs.bottom_bar.ProfileNavScreen


@Composable
fun EditCalculateBolusScreen(
    navController: NavController,
    viewModel: EditCalculateBolusViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)


    LaunchedEffect(action) {
        when (action) {
            EditCalculateBolusSideEffect.NavigateBolusInjection -> navController.navigate(BolusNavScreen.BolusInjection.route)
            EditCalculateBolusSideEffect.NavigateProfile -> navController.navigate(ProfileNavScreen.Profile.route)
            EditCalculateBolusSideEffect.NavigateBack -> navController.navigateUp()
            else -> Unit
        }
    }

    val scrollState = rememberScrollState()
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
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "EditCalculateBolus",
            modifier = Modifier
                .fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            BaseButton(
                onClick = { eventHandler.invoke(EditCalculateBolusEvent.OnInjectButtonClick) },
                text = stringResource(R.string.inject),
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
                onClick = { eventHandler.invoke(EditCalculateBolusEvent.OnCancelingButtonClick) },
                text = stringResource(R.string.canceling),
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