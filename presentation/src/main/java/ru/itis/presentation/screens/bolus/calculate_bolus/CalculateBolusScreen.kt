package ru.itis.presentation.screens.bolus.calculate_bolus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
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
fun CalculateBolusScreen(
    navController: NavController,
    viewModel: CalculateBolusViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            CalculateBolusSideEffect.NavigateEditCalculateBolus -> navController.navigate(BolusNavScreen.EditCalculateBolus.route)
            CalculateBolusSideEffect.NavigateProfile -> navController.navigate(ProfileNavScreen.Profile.route)
            CalculateBolusSideEffect.NavigateBack -> navController.navigateUp()
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
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { eventHandler.invoke(CalculateBolusEvent.OnBackButtonClick) }
            ) {
                Icon(
                    painterResource(R.drawable.ic_arrow_left),
                    contentDescription = "icon_back",
                    modifier = Modifier
                        .size(24.dp),
                    MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        Text(
            text = stringResource(R.string.calculation),
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
        CalculationCard()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            BaseButton(
                onClick = { eventHandler.invoke(CalculateBolusEvent.OnContinueButtonClick) },
                text = stringResource(R.string.continue_),
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
                onClick = { eventHandler.invoke(CalculateBolusEvent.OnCancelingButtonClick) },
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

@Composable
fun CalculationCard(){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CalculationCardTextRow(
                title = stringResource(R.string.current_glucose),
                value = "123"
            )
            CalculationCardTextRow(
                title = stringResource(R.string.current_glucose),
                value = "123"
            )
            CalculationCardTextRow(
                title = stringResource(R.string.current_glucose),
                value = "123"
            )
            Text(
                text = stringResource(R.string.total),
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceTint)
            ) {
                Text(
                    text = stringResource(R.string.total),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Composable
fun CalculationCardTextRow(
    title: String,
    value: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            modifier = Modifier,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = value,
            modifier = Modifier,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}